package com.homegarden.store.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homegarden.store.backend.converter.ProductConverter;
import com.homegarden.store.backend.exception.ProductNotFoundException;
import com.homegarden.store.backend.model.dto.CreateProductDto;
import com.homegarden.store.backend.model.dto.ProductDto;
import com.homegarden.store.backend.model.entity.Product;
import com.homegarden.store.backend.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductConverter productConverter;

    @Autowired
    private ObjectMapper objectMapper;

    private final Timestamp testTimestamp = new Timestamp(System.currentTimeMillis());
    @Test
    void createProduct_ShouldReturnCreatedProduct() throws Exception {
        CreateProductDto createDto = new CreateProductDto();
        createDto.setName("Test Product");
        createDto.setDescription("Test Desc");
        createDto.setPrice(999.99);
        createDto.setCategoryId("1");

        Product productEntity = Product.builder()
                .productId(1L)
                .name("Test Product")
                .description("Test Desc")
                .price(999.99)
                .categoryId(1L)
                .createdAt(testTimestamp)
                .build();

        ProductDto expectedDto = ProductDto.builder()
                .productId(1L)
                .name("Test Product")
                .description("Test Desc")
                .price(999.99)
                .categoryId("1")
                .createdAt(testTimestamp)
                .build();

        when(productConverter.toEntity(any(CreateProductDto.class))).thenReturn(productEntity);
        when(productService.create(any(Product.class))).thenReturn(productEntity);
        when(productConverter.toDto(any(Product.class))).thenReturn(expectedDto);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedDto)));

        verify(productConverter).toEntity(any());
        verify(productService).create(any());
        verify(productConverter).toDto(any());
    }
    @Test
    void getAllProducts_ShouldReturnAllProducts() throws Exception {
        Product product1 = Product.builder().productId(1L).name("P1").price(100.0).build();
        Product product2 = Product.builder().productId(2L).name("P2").price(200.0).build();

        ProductDto dto1 = ProductDto.builder().productId(1L).name("P1").price(100.0).build();
        ProductDto dto2 = ProductDto.builder().productId(2L).name("P2").price(200.0).build();

        when(productService.getAll()).thenReturn(List.of(product1, product2));
        when(productConverter.toDto(product1)).thenReturn(dto1);
        when(productConverter.toDto(product2)).thenReturn(dto2);

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(productService).getAll();
        verify(productConverter, times(2)).toDto(any(Product.class));
    }
    @Test
    void getProductById_WhenProductExists_ShouldReturnProduct() throws Exception {
        Long productId = 1L;

        Product product = Product.builder().productId(productId).name("P1").price(100.0).build();
        ProductDto dto = ProductDto.builder().productId(productId).name("P1").price(100.0).build();

        when(productService.getById(productId)).thenReturn(product);
        when(productConverter.toDto(product)).thenReturn(dto);

        mockMvc.perform(get("/products/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(productId));

        verify(productService).getById(productId);
        verify(productConverter).toDto(product);
    }
    @Test
    void getProductById_WhenProductNotExists_ShouldReturnNotFound() throws Exception {
        Long productId = 999L;

        when(productService.getById(productId)).thenThrow(new ProductNotFoundException("Product not found"));

        mockMvc.perform(get("/products/{id}", productId))
                .andExpect(status().isNotFound());

        verify(productService).getById(productId);
    }
    @Test
    void deleteProduct_ShouldReturnNoContent() throws Exception {
        Long productId = 1L;

        doNothing().when(productService).delete(productId);

        mockMvc.perform(delete("/products/{id}", productId))
                .andExpect(status().isNoContent());

        verify(productService).delete(productId);
    }
    @Test
    void updateProduct_ShouldReturnUpdatedProduct() throws Exception {
        Long productId = 1L;

        CreateProductDto updateDto = new CreateProductDto();
        updateDto.setName("Updated");
        updateDto.setDescription("Updated Desc");
        updateDto.setPrice(1500.0);
        updateDto.setCategoryId("2");

        Product toUpdate = Product.builder().productId(productId).name("Updated").price(1500.0).build();
        Product updated = Product.builder().productId(productId).name("Updated").price(1500.0).updatedAt(testTimestamp).build();

        ProductDto expectedDto = ProductDto.builder().productId(productId).name("Updated").price(1500.0).updatedAt(testTimestamp).build();

        when(productConverter.toEntity(any(CreateProductDto.class))).thenReturn(toUpdate);
        when(productService.update(any(Product.class))).thenReturn(updated);
        when(productConverter.toDto(updated)).thenReturn(expectedDto);

        mockMvc.perform(put("/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedDto)));

        verify(productConverter).toEntity(any());
        verify(productService).update(any());
        verify(productConverter).toDto(updated);
    }
    @Test
    void updateProduct_WhenProductNotExists_ShouldReturnNotFound() throws Exception {
        Long productId = 999L;
        CreateProductDto updateDto = new CreateProductDto();

        Product toUpdate = new Product();
        toUpdate.setProductId(productId);

        when(productConverter.toEntity(any(CreateProductDto.class))).thenReturn(toUpdate);
        when(productService.update(toUpdate)).thenThrow(new ProductNotFoundException("Product not found"));

        mockMvc.perform(put("/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound());

        verify(productConverter).toEntity(any());
        verify(productService).update(toUpdate);
    }
}

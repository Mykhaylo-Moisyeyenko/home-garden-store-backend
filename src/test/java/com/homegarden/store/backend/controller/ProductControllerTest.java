package com.homegarden.store.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homegarden.store.backend.converter.ProductConverter;
import com.homegarden.store.backend.exception.ProductNotFoundException;
import com.homegarden.store.backend.model.dto.CreateProductDto;
import com.homegarden.store.backend.model.dto.ProductDto;
import com.homegarden.store.backend.model.entity.Product;
import com.homegarden.store.backend.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    private Timestamp testTimestamp;

    private CreateProductDto createProductDto;
    private Product productEntity;
    private ProductDto expectedProductDto;

    private Product product1;
    private Product product2;
    private ProductDto productDto1;
    private ProductDto productDto2;

    private CreateProductDto updateProductDto;
    private Product productToUpdate;
    private Product updatedProduct;
    private ProductDto updatedProductDto;

    @BeforeEach
    void setUp() {
        testTimestamp = new Timestamp(System.currentTimeMillis());

        // Create product data
        createProductDto = new CreateProductDto();
        createProductDto.setName("Test Product");
        createProductDto.setDescription("Test Desc");
        createProductDto.setPrice(999.99);
        createProductDto.setCategoryId("1");

        productEntity = Product.builder()
                .productId(1L)
                .name("Test Product")
                .description("Test Desc")
                .price(999.99)
                .categoryId(1L)
                .createdAt(testTimestamp)
                .build();

        expectedProductDto = ProductDto.builder()
                .productId(1L)
                .name("Test Product")
                .description("Test Desc")
                .price(999.99)
                .categoryId("1")
                .createdAt(testTimestamp)
                .build();

        // Get all products data
        product1 = Product.builder().productId(1L).name("P1").price(100.0).build();
        product2 = Product.builder().productId(2L).name("P2").price(200.0).build();
        productDto1 = ProductDto.builder().productId(1L).name("P1").price(100.0).build();
        productDto2 = ProductDto.builder().productId(2L).name("P2").price(200.0).build();

        // Update product data
        updateProductDto = new CreateProductDto();
        updateProductDto.setName("Updated");
        updateProductDto.setDescription("Updated Desc");
        updateProductDto.setPrice(1500.0);
        updateProductDto.setCategoryId("2");

        productToUpdate = Product.builder()
                .productId(1L)
                .name("Updated")
                .description("Updated Desc")
                .price(1500.0)
                .categoryId(2L)
                .build();

        updatedProduct = Product.builder()
                .productId(1L)
                .name("Updated")
                .description("Updated Desc")
                .price(1500.0)
                .categoryId(2L)
                .updatedAt(testTimestamp)
                .build();

        updatedProductDto = ProductDto.builder()
                .productId(1L)
                .name("Updated")
                .description("Updated Desc")
                .price(1500.0)
                .categoryId("2")
                .updatedAt(testTimestamp)
                .build();
    }

    @Test
    void createProduct_ShouldReturnCreatedProduct() throws Exception {
        when(productConverter.toEntity(any(CreateProductDto.class))).thenReturn(productEntity);
        when(productService.create(any(Product.class))).thenReturn(productEntity);
        when(productConverter.toDto(any(Product.class))).thenReturn(expectedProductDto);

        mockMvc.perform(post("/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createProductDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedProductDto)));

        verify(productConverter).toEntity(any());
        verify(productService).create(any());
        verify(productConverter).toDto(any());
    }

    @Test
    void getAllProducts_ShouldReturnAllProducts() throws Exception {
        when(productService.getAll()).thenReturn(List.of(product1, product2));
        when(productConverter.toDto(product1)).thenReturn(productDto1);
        when(productConverter.toDto(product2)).thenReturn(productDto2);

        mockMvc.perform(get("/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(productService).getAll();
        verify(productConverter, times(2)).toDto(any(Product.class));
    }

    @Test
    void getProductById_WhenProductExists_ShouldReturnProduct() throws Exception {
        Long productId = 1L;

        when(productService.getById(productId)).thenReturn(product1);
        when(productConverter.toDto(product1)).thenReturn(productDto1);

        mockMvc.perform(get("/v1/products/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(productId));

        verify(productService).getById(productId);
        verify(productConverter).toDto(product1);
    }

    @Test
    void getProductById_WhenProductNotExists_ShouldReturnNotFound() throws Exception {
        Long productId = 999L;

        when(productService.getById(productId)).thenThrow(new ProductNotFoundException("Product not found"));

        mockMvc.perform(get("/v1/products/{id}", productId))
                .andExpect(status().isNotFound());

        verify(productService).getById(productId);
    }

    @Test
    void deleteProduct_ShouldReturnNoContent() throws Exception {
        Long productId = 1L;

        doNothing().when(productService).delete(productId);

        mockMvc.perform(delete("/v1/products/{id}", productId))
                .andExpect(status().isNoContent());

        verify(productService).delete(productId);
    }

    @Test
    void updateProduct_ShouldReturnUpdatedProduct() throws Exception {
        when(productConverter.toEntity(any(CreateProductDto.class))).thenReturn(productToUpdate);
        when(productService.update(any(Product.class))).thenReturn(updatedProduct);
        when(productConverter.toDto(updatedProduct)).thenReturn(updatedProductDto);

        mockMvc.perform(put("/v1/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateProductDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedProductDto)));

        verify(productConverter).toEntity(any());
        verify(productService).update(any());
        verify(productConverter).toDto(updatedProduct);
    }

    @Test
    void updateProduct_WhenProductNotExists_ShouldReturnNotFound() throws Exception {
        Long productId = 999L;

        when(productConverter.toEntity(any(CreateProductDto.class))).thenReturn(productToUpdate);
        when(productService.update(any(Product.class)))
                .thenThrow(new ProductNotFoundException("Product not found"));

        mockMvc.perform(put("/v1/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateProductDto)))
                .andExpect(status().isNotFound());

        verify(productConverter).toEntity(any());
        verify(productService).update(any(Product.class));
    }
}
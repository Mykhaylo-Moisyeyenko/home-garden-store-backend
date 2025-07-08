package com.homegarden.store.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homegarden.store.backend.converter.ProductConverter;
import com.homegarden.store.backend.model.dto.CreateProductDto;
import com.homegarden.store.backend.model.dto.ProductDto;
import com.homegarden.store.backend.model.entity.Product;
import com.homegarden.store.backend.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductConverter productConverter;

    @Test
    @DisplayName("POST /v1/products should return created product")
    void createProduct_ShouldReturnCreatedProduct() throws Exception {
        CreateProductDto createDto = new CreateProductDto("Onion", "Red onion", 29.99, 1L, null);
        Product product = Product.builder().name("Onion").description("Red onion").price(29.99).build();
        Product created = Product.builder().productId(1L).name("Onion").description("Red onion").price(29.99).build();
        ProductDto expectedDto = ProductDto.builder()
                .productId(1L).name("Onion").description("Red onion").price(29.99).categoryId(1L).build();

        when(productConverter.toEntity(createDto)).thenReturn(product);
        when(productService.create(product)).thenReturn(created);
        when(productConverter.toDto(created)).thenReturn(expectedDto);

        mockMvc.perform(post("/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(1L))
                .andExpect(jsonPath("$.name").value("Onion"));
    }

    @Test
    @DisplayName("GET /v1/products should return product list")
    void getAllProducts_ShouldReturnList() throws Exception {
        Product product = Product.builder().productId(1L).name("Onion").description("Red").price(29.99).build();
        ProductDto dto = ProductDto.builder().productId(1L).name("Onion").description("Red").price(29.99).build();

        when(productService.getAll()).thenReturn(List.of(product));
        when(productConverter.toDto(product)).thenReturn(dto);

        mockMvc.perform(get("/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Onion"));
    }

    @Test
    @DisplayName("GET /v1/products/{id} should return product by ID")
    void getProductById_ShouldReturnProduct() throws Exception {
        Product product = Product.builder().productId(1L).name("Onion").description("Red").price(29.99).build();
        ProductDto dto = ProductDto.builder().productId(1L).name("Onion").description("Red").price(29.99).build();

        when(productService.getById(1L)).thenReturn(product);
        when(productConverter.toDto(product)).thenReturn(dto);

        mockMvc.perform(get("/v1/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(1L))
                .andExpect(jsonPath("$.name").value("Onion"));
    }

    @Test
    @DisplayName("PUT /v1/products/{id} should update product")
    void updateProduct_ShouldReturnUpdatedProduct() throws Exception {
        CreateProductDto updateDto = new CreateProductDto("Onion", "Green onion", 25.99, 1L, null);
        Product productToUpdate = Product.builder().productId(1L).name("Onion").description("Green onion").price(25.99).build();
        Product updated = Product.builder().productId(1L).name("Onion").description("Green onion").price(25.99).build();
        ProductDto dto = ProductDto.builder().productId(1L).name("Onion").description("Green onion").price(25.99).build();

        when(productConverter.toEntity(updateDto)).thenReturn(productToUpdate);
        when(productService.update(productToUpdate)).thenReturn(updated);
        when(productConverter.toDto(updated)).thenReturn(dto);

        mockMvc.perform(put("/v1/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(1L))
                .andExpect(jsonPath("$.description").value("Green onion"));
    }

    @Test
    @DisplayName("DELETE /v1/products/{id} should return no content")
    void deleteProduct_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/v1/products/1"))
                .andExpect(status().isNoContent());
    }
}


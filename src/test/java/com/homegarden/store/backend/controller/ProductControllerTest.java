package com.homegarden.store.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homegarden.store.backend.converter.ProductConverter;
import com.homegarden.store.backend.dto.CreateProductDto;
import com.homegarden.store.backend.dto.ProductDto;
import com.homegarden.store.backend.entity.Category;
import com.homegarden.store.backend.entity.Product;
import com.homegarden.store.backend.exception.ProductNotFoundException;
import com.homegarden.store.backend.service.ProductService;
import com.homegarden.store.backend.service.security.JwtFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private ProductConverter productConverter;

    @MockitoBean
    private JwtFilter jwtFilter;

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

    public static Stream<Arguments> productGetAllParams() {
        return Stream.of(
                Arguments.of(null, null, null, null, null, "/v1/products"),
                Arguments.of(1L, null, null, null, null, "/v1/products?categoryId=1"),
                Arguments.of(null, new BigDecimal("15.00"), null, null, null, "/v1/products?minPrice=15.00"),
                Arguments.of(null, null, new BigDecimal("40.00"), null, null, "/v1/products?maxPrice=40.00"),
                Arguments.of(null, null, null, true, null, "/v1/products?discount=true"),
                Arguments.of(null, null, null, null, "DESC", "/v1/products?sort=DESC"),
                Arguments.of(2L, new BigDecimal("20.00"), new BigDecimal("50.00"), false, "ASC", "/v1/products?categoryId=2&minPrice=20.00&maxPrice=50.00&discount=false&sort=ASC")
        );
    }

    @BeforeEach
    void setUp() {
        Timestamp testTimestamp = Timestamp.valueOf(LocalDateTime.now());

        createProductDto = CreateProductDto.builder()
                .name("Test Product")
                .description("Test Desc")
                .price(new BigDecimal("999.99"))
                .categoryId(1L)
                .build();

        Category category = new Category(1L, "Test Category", new ArrayList<>());

        productEntity = Product.builder()
                .productId(1L)
                .name("Test Product")
                .description("Test Desc")
                .price(new BigDecimal("999.99"))
                .category(category)
                .createdAt(testTimestamp)
                .updatedAt(testTimestamp)
                .build();

        expectedProductDto = ProductDto.builder()
                .productId(1L)
                .name("Test Product")
                .description("Test Desc")
                .price(new BigDecimal("999.99"))
                .categoryId(1L)
                .createdAt(testTimestamp)
                .updatedAt(testTimestamp)
                .build();

        product1 = Product.builder().productId(1L).name("P1").price(new BigDecimal("100.0")).build();
        product2 = Product.builder().productId(2L).name("P2").price(new BigDecimal("200.0")).build();
        productDto1 = ProductDto.builder().productId(1L).name("P1").price(new BigDecimal("100.0")).build();
        productDto2 = ProductDto.builder().productId(2L).name("P2").price(new BigDecimal("200.0")).build();

        updateProductDto = CreateProductDto.builder()
                .name("Updated")
                .description("Updated Desc")
                .price(new BigDecimal("1500.0"))
                .categoryId(1L)
                .build();

        productToUpdate = Product.builder()
                .productId(1L)
                .name("Updated")
                .description("Updated Desc")
                .price(new BigDecimal("1500.0"))
                .category(category)
                .build();

        updatedProduct = Product.builder()
                .productId(1L)
                .name("Updated")
                .description("Updated Desc")
                .price(new BigDecimal("1500.0"))
                .category(category)
                .updatedAt(testTimestamp)
                .build();

        updatedProductDto = ProductDto.builder()
                .productId(1L)
                .name("Updated")
                .description("Updated Desc")
                .price(new BigDecimal("1500.0"))
                .categoryId(1L)
                .updatedAt(testTimestamp)
                .build();
    }

    @Test
    @DisplayName("GET /v1/products should return created product")
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

    @ParameterizedTest
    @MethodSource("productGetAllParams")
    @DisplayName("GET /v1/products should return list of filtered products")
    void getAllProducts_WithParams_ShouldReturnProducts(
            Long categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean discount,
            String sort,
            String url
    ) throws Exception {
        when(productService.getAll(categoryId, minPrice, maxPrice, discount, sort))
                .thenReturn(List.of(product1, product2));
        when(productConverter.toDto(product1)).thenReturn(productDto1);
        when(productConverter.toDto(product2)).thenReturn(productDto2);

        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].productId").value(1))
                .andExpect(jsonPath("$[1].productId").value(2));

        verify(productService).getAll(categoryId, minPrice, maxPrice, discount, sort);
        verify(productConverter, times(2)).toDto(any(Product.class));
    }

    @Test
    @DisplayName("GET /v1/products/{id} should return product by ID")
    void getProductById_ShouldReturnProduct() throws Exception {

        when(productService.getById(1L)).thenReturn(product1);
        when(productConverter.toDto(product1)).thenReturn(productDto1);

        mockMvc.perform(get("/v1/products/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(1L))
                .andExpect(jsonPath("$.name").value("P1"));
        verify(productService).getById(1L);
        verify(productConverter).toDto(product1);
    }

    @Test
    @DisplayName("GET /v1/products/{id} should fail with wrong ID")
    void getProductById_WhenProductNotExists_ShouldReturnNotFound() throws Exception {
        Long productId = 999L;

        when(productService.getById(productId)).thenThrow(new ProductNotFoundException("Product not found"));

        mockMvc.perform(get("/v1/products/{id}", productId))
                .andExpect(status().isNotFound());

        verify(productService).getById(productId);
    }

    @Test
    @DisplayName("DELETE /v1/products/{id} should return NO CONTENT")
    void deleteProduct_ShouldReturnNoContent() throws Exception {
        Long productId = 1L;

        doNothing().when(productService).delete(productId);

        mockMvc.perform(delete("/v1/products/{id}", productId))
                .andExpect(status().isNoContent());

        verify(productService).delete(productId);
    }

    @Test
    @DisplayName("PUT /v1/products/{id} should update product")
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
        verify(productService).update(productToUpdate);
        verify(productConverter).toDto(updatedProduct);
    }

    @Test
    @DisplayName("PUT /v1/products/{id} should return NOT FOUND")
    void updateProduct_WhenProductNotExists_ShouldReturnNotFound() throws Exception {
        Long productId = 999L;

        when(productConverter.toEntity(any(CreateProductDto.class))).thenReturn(productToUpdate);
        when(productService.update(productToUpdate))
                .thenThrow(new ProductNotFoundException("Product not found"));

        mockMvc.perform(put("/v1/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateProductDto)))
                .andExpect(status().isNotFound());

        verify(productConverter).toEntity(any());
        verify(productService).update(productToUpdate);
    }
}
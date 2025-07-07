package com.homegarden.store.backend.controller;

import com.homegarden.store.backend.converter.ProductConverter;
import com.homegarden.store.backend.exception.ProductNotFoundException;
import com.homegarden.store.backend.model.dto.CreateProductDto;
import com.homegarden.store.backend.model.dto.ProductDto;
import com.homegarden.store.backend.model.entity.Product;
import com.homegarden.store.backend.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private ProductConverter productConverter;

    @InjectMocks
    private ProductController productController;

    private final Timestamp testTimestamp = new Timestamp(System.currentTimeMillis());

    @Test
    void createProduct_ShouldReturnCreatedProduct() {
        // Arrange
        CreateProductDto createDto = new CreateProductDto();
        createDto.setName("");
        createDto.setDescription("");
        createDto.setPrice(999.99);
        createDto.setCategoryId("");

        Product productEntity = Product.builder()
                .productId(1L)
                .name("")
                .description("")
                .price(999.99)
                .categoryId(1L)
                .createdAt(testTimestamp)
                .build();

        ProductDto expectedDto = ProductDto.builder()
                .productId(1L)
                .name("")
                .description("")
                .price(999.99)
                .categoryId("")
                .createdAt(testTimestamp)
                .build();

        when(productConverter.toEntity(createDto)).thenReturn(productEntity);
        when(productService.create(productEntity)).thenReturn(productEntity);
        when(productConverter.toDto(productEntity)).thenReturn(expectedDto);

        // Act
        ResponseEntity<ProductDto> response = productController.create(createDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedDto, response.getBody());
        verify(productConverter).toEntity(createDto);
        verify(productService).create(productEntity);
        verify(productConverter).toDto(productEntity);
    }

    @Test
    void getAllProducts_ShouldReturnAllProducts() {
        // Arrange
        Product product1 = Product.builder()
                .productId(1L)
                .name("")
                .price(999.99)
                .build();

        Product product2 = Product.builder()
                .productId(2L)
                .name("")
                .price(799.99)
                .build();

        ProductDto dto1 = ProductDto.builder()
                .productId(1L)
                .name("")
                .price(999.99)
                .build();

        ProductDto dto2 = ProductDto.builder()
                .productId(2L)
                .name("")
                .price(799.99)
                .build();

        when(productService.getAll()).thenReturn(List.of(product1, product2));
        when(productConverter.toDto(product1)).thenReturn(dto1);
        when(productConverter.toDto(product2)).thenReturn(dto2);

        ResponseEntity<List<ProductDto>> response = productController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertTrue(response.getBody().contains(dto1));
        assertTrue(response.getBody().contains(dto2));
        verify(productService).getAll();
        verify(productConverter, times(2)).toDto(any(Product.class));
    }

    @Test
    void getProductById_WhenProductExists_ShouldReturnProduct() {

        Long productId = 1L;
        Product product = Product.builder()
                .productId(productId)
                .name("")
                .price(999.99)
                .build();

        ProductDto expectedDto = ProductDto.builder()
                .productId(productId)
                .name("")
                .price(999.99)
                .build();

        when(productService.getById(productId)).thenReturn(product);
        when(productConverter.toDto(product)).thenReturn(expectedDto);

        ResponseEntity<ProductDto> response = productController.getById(productId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDto, response.getBody());
        verify(productService).getById(productId);
        verify(productConverter).toDto(product);
    }

    @Test
    void getProductById_WhenProductNotExists_ShouldThrowException() {
        Long productId = 999L;
        when(productService.getById(productId)).thenThrow(new ProductNotFoundException("Product not found"));

        assertThrows(ProductNotFoundException.class, () -> productController.getById(productId));
        verify(productService).getById(productId);
        verify(productConverter, never()).toDto(any());
    }

    @Test
    void deleteProduct_ShouldReturnNoContent() {
        Long productId = 1L;
        doNothing().when(productService).delete(productId);

        ResponseEntity<Void> response = productController.delete(productId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(productService).delete(productId);
    }

    @Test
    void updateProduct_ShouldReturnUpdatedProduct() {

        Long productId = 1L;
        CreateProductDto updateDto = new CreateProductDto();
        updateDto.setName("");
        updateDto.setDescription("");
        updateDto.setPrice(1299.99);
        updateDto.setCategoryId("");

        Product productToUpdate = Product.builder()
                .productId(productId)
                .name("")
                .description("")
                .price(1299.99)
                .categoryId(2L)
                .build();

        Product updatedProduct = Product.builder()
                .productId(productId)
                .name("")
                .description("")
                .price(1299.99)
                .categoryId(2L)
                .updatedAt(testTimestamp)
                .build();

        ProductDto expectedDto = ProductDto.builder()
                .productId(productId)
                .name("")
                .description("")
                .price(1299.99)
                .categoryId("")
                .updatedAt(testTimestamp)
                .build();

        when(productConverter.toEntity(updateDto)).thenReturn(productToUpdate);
        when(productService.update(productToUpdate)).thenReturn(updatedProduct);
        when(productConverter.toDto(updatedProduct)).thenReturn(expectedDto);

        ResponseEntity<ProductDto> response = productController.updateProduct(productId, updateDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDto, response.getBody());
        assertEquals(productId, productToUpdate.getProductId());
        verify(productConverter).toEntity(updateDto);
        verify(productService).update(productToUpdate);
        verify(productConverter).toDto(updatedProduct);
    }

    @Test
    void updateProduct_WhenProductNotExists_ShouldThrowException() {
        Long productId = 999L;
        CreateProductDto updateDto = new CreateProductDto();
        Product productToUpdate = new Product();
        productToUpdate.setProductId(productId);

        when(productConverter.toEntity(updateDto)).thenReturn(productToUpdate);
        when(productService.update(productToUpdate)).thenThrow(new ProductNotFoundException("Product not found"));

        assertThrows(ProductNotFoundException.class,
                () -> productController.updateProduct(productId, updateDto));
        verify(productConverter).toEntity(updateDto);
        verify(productService).update(productToUpdate);
        verify(productConverter, never()).toDto(any());
    }
}

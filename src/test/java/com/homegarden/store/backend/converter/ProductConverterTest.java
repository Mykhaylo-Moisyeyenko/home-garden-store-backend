package com.homegarden.store.backend.converter;

import com.homegarden.store.backend.dto.CreateProductDto;
import com.homegarden.store.backend.dto.ProductDto;
import com.homegarden.store.backend.entity.Category;
import com.homegarden.store.backend.entity.Product;
import com.homegarden.store.backend.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductConverterTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private ProductConverter productConverter;

    Timestamp testTimestamp = Timestamp.valueOf(LocalDateTime.now());

    CreateProductDto createProductDto = CreateProductDto.builder()
            .name("Test Product")
            .description("Test Desc")
            .price(new BigDecimal("999.99"))
            .categoryId(1L)
            .imageUrl("Test Image")
            .discountPrice(new BigDecimal("888.88"))
            .build();

    Category category = new Category(1L, "Test Category", new ArrayList<>());

    Product product = Product.builder()
            .productId(1L)
            .name("Test Product")
            .description("Test Desc")
            .price(new BigDecimal("999.99"))
            .category(category)
            .imageUrl("Test Image")
            .discountPrice(new BigDecimal("888.88"))
            .createdAt(testTimestamp)
            .updatedAt(testTimestamp)
            .build();

    ProductDto productDto = ProductDto.builder()
            .productId(1L)
            .name("Test Product")
            .description("Test Desc")
            .price(new BigDecimal("999.99"))
            .categoryId(1L)
            .imageUrl("Test Image")
            .discountPrice(new BigDecimal("888.88"))
            .createdAt(testTimestamp)
            .updatedAt(testTimestamp)
            .build();

    @Test
    void toEntityTest() {
        when(categoryService.getById(1L)).thenReturn(category);

        Product result = productConverter.toEntity(createProductDto);

        assertNull(result.getProductId());
        assertNotNull(result);
        assertEquals(createProductDto.getName(), result.getName());
        assertEquals(createProductDto.getDescription(), result.getDescription());
        assertEquals(createProductDto.getPrice(), result.getPrice());
        assertEquals(createProductDto.getCategoryId(), result.getCategory().getCategoryId());
        assertEquals(createProductDto.getImageUrl(), result.getImageUrl());
        assertEquals(createProductDto.getDiscountPrice(), result.getDiscountPrice());
    }

    @Test
    void toDtoTest() {

        ProductDto result = productConverter.toDto(product);

        assertNotNull(result);
        assertEquals(productDto.productId(), result.productId());
        assertEquals(productDto.name(), result.name());
        assertEquals(productDto.description(), result.description());
        assertEquals(productDto.price(), result.price());
        assertEquals(productDto.categoryId(), result.categoryId());
        assertEquals(productDto.imageUrl(), result.imageUrl());
        assertEquals(productDto.discountPrice(), result.discountPrice());
        assertEquals(productDto.createdAt(), result.createdAt());
        assertEquals(productDto.updatedAt(), result.updatedAt());
    }
}
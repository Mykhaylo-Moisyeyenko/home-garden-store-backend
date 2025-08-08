package com.homegarden.store.backend.converter;

import com.homegarden.store.backend.dto.CreateProductDto;
import com.homegarden.store.backend.dto.ProductDto;
import com.homegarden.store.backend.entity.Product;
import com.homegarden.store.backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductConverter implements Converter<Product, CreateProductDto, ProductDto> {

    private final CategoryService categoryService;

    @Override
    public Product toEntity(CreateProductDto createProductDto) {

        return Product.builder()
                .name(createProductDto.getName())
                .description(createProductDto.getDescription())
                .price(createProductDto.getPrice())
                .category(categoryService.getById(createProductDto.getCategoryId()))
                .imageUrl(createProductDto.getImageUrl())
                .discountPrice(createProductDto.getDiscountPrice())
                .build();
    }

    @Override
    public ProductDto toDto(Product product) {

        return ProductDto.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .categoryId(product.getCategory().getCategoryId())
                .imageUrl(product.getImageUrl())
                .discountPrice(product.getDiscountPrice())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
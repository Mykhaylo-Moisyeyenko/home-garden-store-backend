package com.homegarden.store.backend.converter;

import com.homegarden.store.backend.dto.CreateProductDto;
import com.homegarden.store.backend.dto.ProductDto;
import com.homegarden.store.backend.entity.Product;
import com.homegarden.store.backend.entity.Category;
import com.homegarden.store.backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductConverter implements Converter<Product, CreateProductDto, ProductDto> {

    private final CategoryService categoryService;

    @Override
    public Product toEntity(CreateProductDto createProductDto) {
        Product product = new Product();
        product.setName(createProductDto.getName());
        product.setDescription(createProductDto.getDescription());
        product.setPrice(createProductDto.getPrice());
        Category category = categoryService.getById(createProductDto.getCategoryId());
        product.setCategory(category);
        product.setImageUrl(createProductDto.getImageUrl());
        product.setDiscountPrice(createProductDto.getDiscountPrice());

        return product;
    }

    @Override
    public ProductDto toDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setProductId(product.getProductId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setCategoryId(product.getCategory().getCategoryId());
        dto.setImageUrl(product.getImageUrl());
        dto.setDiscountPrice(product.getDiscountPrice());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());
        return dto;
    }
}
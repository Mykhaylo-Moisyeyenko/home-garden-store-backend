package com.homegarden.store.backend.converter;

import com.homegarden.store.backend.model.dto.CreateProductDto;
import com.homegarden.store.backend.model.dto.ProductDto;
import com.homegarden.store.backend.model.entity.Category;
import com.homegarden.store.backend.model.entity.Product;
import com.homegarden.store.backend.service.CategoryService;
import org.springframework.stereotype.Component;

@Component
public class ProductConverter implements Converter<Product, CreateProductDto, ProductDto> {

    private final CategoryService categoryService;

    public ProductConverter(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public Product toEntity(CreateProductDto createProductDto) {
        Product product = new Product();
        product.setName(createProductDto.getName());
        product.setDescription(createProductDto.getDescription());
        product.setPrice(createProductDto.getPrice());
        Category category = categoryService.getById(createProductDto.getCategoryId());
        product.setCategory(category);

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
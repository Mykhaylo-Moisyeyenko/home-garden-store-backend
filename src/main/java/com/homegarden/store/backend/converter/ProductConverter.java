package com.homegarden.store.backend.converter;

import com.homegarden.store.backend.model.dto.CreateProductDto;
import com.homegarden.store.backend.model.dto.ProductDto;
import com.homegarden.store.backend.model.entity.Category;
import com.homegarden.store.backend.model.entity.Product;
import com.homegarden.store.backend.repository.CategoryRepository;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
public class ProductConverter implements Converter<Product, CreateProductDto, ProductDto> {

    private final CategoryRepository categoryRepository;

    public ProductConverter(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    @Override
    public Product toEntity(CreateProductDto createProductDto) {
        Product product = new Product();
        product.setName(createProductDto.getName());
        product.setDescription(createProductDto.getDescription());
        product.setPrice(createProductDto.getPrice());
        Category category = categoryRepository.findById(Long.valueOf(createProductDto.getCategoryId()))
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + createProductDto.getCategoryId()));
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
        dto.setCategoryId(Long.valueOf(product.getCategory().getCategoryId()));
        dto.setImageUrl(product.getImageUrl());
        dto.setDiscountPrice(product.getDiscountPrice());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());
        return dto;
    }
}
package com.homegarden.store.backend.mapper;

import com.homegarden.store.backend.model.dto.ProductDto;
import com.homegarden.store.backend.model.entity.Product;
import org.springframework.web.bind.annotation.Mapping;

public @interface Mapper {
    @Mapping(source = "category.id", target = "categoryId")
    ProductDto toDto(Product product);

    @Mapping(source = "categoryId", target = "category.id")
    Product toEntity(ProductDto dto);

    String componentModel();
}

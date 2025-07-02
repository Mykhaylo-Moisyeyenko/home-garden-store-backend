
package com.example.project.converter;

import com.example.project.dto.ProductDto;
import com.example.project.entity.Category;
import com.example.project.entity.Product;
import com.homegarden.store.backend.mapper.Mapper;
import org.mapstruct.*;
import org.springframework.web.bind.annotation.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "category.id", target = "categoryId")
    ProductDto toDto(Product product);

    @Mapping(source = "categoryId", target = "category.id")
    Product toEntity(ProductDto dto);
}


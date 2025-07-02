package com.homegarden.store.backend.mapper;

import com.homegarden.store.backend.model.dto.ProductDto;
import com.homegarden.store.backend.model.entity.Product;
import org.springframework.web.bind.annotation.Mapping;

public interface ProductMapper {
    // Если ProductDto и Product имеют разные имена полей,
    // можно использовать @Mapping для их соответствия

    // Преобразование из DTO в Entity
    @Mapping(target = "category", ignore = true) // категорию устанавливаешь вручную
    Product toEntity(ProductDto productDto);

    // Преобразование из Entity в DTO
    @Mapping(target = "categoryId", source = "category.id")
    ProductDto toDto(Product product);
}

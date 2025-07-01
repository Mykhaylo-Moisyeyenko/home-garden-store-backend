package com.homegarden.store.backend.converter;

import com.homegarden.store.backend.model.dto.CategoryDto;
import com.homegarden.store.backend.model.entity.Category;

public class CategoryConverter {

    public static CategoryDto toDto(Category category) {
        return new CategoryDto(category.getCategoryId(), category.getName());
    }

    public static Category toEntity(CategoryDto dto) {
        return Category.builder()
                .categoryId(dto.categoryId())
                .name(dto.name())
                .build();
    }
}

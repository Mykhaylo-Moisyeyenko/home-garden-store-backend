package com.homegarden.store.backend.converter;

import com.homegarden.store.backend.dto.CategoryDto;
import com.homegarden.store.backend.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryConverter {

    public CategoryDto toDto(Category category) {
        return new CategoryDto(category.getCategoryId(), category.getName());
    }

    public Category toEntity(CategoryDto dto) {
        return Category.builder()
                .categoryId(dto.categoryId())
                .name(dto.name())
                .build();
    }
}
package com.homegarden.store.backend.converter;

import com.homegarden.store.backend.dto.CategoryDto;
import com.homegarden.store.backend.entity.Category;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CategoryConverterTest {

    private CategoryConverter converter = new CategoryConverter();

    Category category = new Category(1L, "tools", new ArrayList<>());

    CategoryDto categoryDto = new CategoryDto(1L, "tools");

    @Test
    void toDtoTest() {
       CategoryDto actual =converter.toDto(category);

        assertEquals(1L, actual.categoryId());
        assertEquals("tools", actual.name());
    }

    @Test
    void toEntityTest() {
        Category actual =converter.toEntity(categoryDto);

        assertEquals(1L, actual.getCategoryId());
        assertEquals("tools", actual.getName());
    }
}
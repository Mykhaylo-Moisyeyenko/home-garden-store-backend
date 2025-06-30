package com.homegarden.store.backend.service;

import com.homegarden.store.backend.model.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto update(Long categoryId, CategoryDto dto);
    CategoryDto create(CategoryDto dto);
    List<CategoryDto> getAll();
    CategoryDto getById(Long id);
    void delete(Long id);
}


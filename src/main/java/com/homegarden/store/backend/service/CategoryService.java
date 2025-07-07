package com.homegarden.store.backend.service;

import com.homegarden.store.backend.model.entity.Category;

import java.util.List;

public interface CategoryService {

    Category update(Long categoryId, String name);

    Category create(Category category);

    List<Category> getAll();

    Category getById(Long id);

    void delete(Long id);
}
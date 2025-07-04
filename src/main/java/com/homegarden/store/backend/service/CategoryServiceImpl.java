package com.homegarden.store.backend.service;

import com.homegarden.store.backend.exception.CategoryNotFoundException;
import com.homegarden.store.backend.model.entity.Category;
import com.homegarden.store.backend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getById(Long сategoryId) {
        return categoryRepository.findById(сategoryId).orElseThrow(() -> new CategoryNotFoundException("Category with id " + сategoryId + " not found"));
    }

    @Override
    public Category update(Long categoryId, String name) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category with id " + categoryId + " not found"));
        category.setName(name);
        Category updatedCategory = categoryRepository.save(category);
        return updatedCategory;
    }

    @Override
    public void delete(Long id) {
        Category category = getById(id);
        categoryRepository.delete(category);
    }
}


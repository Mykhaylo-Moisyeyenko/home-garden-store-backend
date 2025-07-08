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
    public Category getById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category with id " + categoryId + " not found"));
    }

    @Override
    public Category update(Long categoryId, String name) {
        Category category = getById(categoryId);
        category.setName(name);
        return categoryRepository.save(category);
    }

    @Override
    public void delete(Long id) {
        categoryRepository.delete(getById(id));
    }
}
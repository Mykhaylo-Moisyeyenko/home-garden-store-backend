package com.homegarden.store.backend.service;

import com.homegarden.store.backend.exception.CategoryNotFoundException;
import com.homegarden.store.backend.model.dto.CategoryDto;
import com.homegarden.store.backend.model.entity.Category;
import com.homegarden.store.backend.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto create(CategoryDto dto) {
        Category category = toEntity(dto);
        Category saved = categoryRepository.save(category);
        return toDto(saved);
    }

    @Override
    public List<CategoryDto> getAll() {
        return categoryRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Категория с id " + id + " не найдена"));
        return toDto(category);
    }


    @Override
    public CategoryDto update(Long categoryId, CategoryDto dto) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Категория с id " + categoryId + " не найдена"));

        category.setName(dto.name());

        Category updatedCategory = categoryRepository.save(category);

        return toDto(updatedCategory);
    }

    @Override
    public void delete(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new CategoryNotFoundException("Категория с id " + categoryId + " не найдена");
        }
        categoryRepository.deleteById(categoryId);
    }



    private CategoryDto toDto(Category category) {
        return new CategoryDto(category.getCategoryId(), category.getName());
    }

    private Category toEntity(CategoryDto dto) {
        return Category.builder()
                .categoryId(dto.categoryId())
                .name(dto.name())
                .build();
    }
}


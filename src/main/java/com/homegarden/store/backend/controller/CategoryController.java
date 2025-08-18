package com.homegarden.store.backend.controller;

import com.homegarden.store.backend.controller.api.CategoryControllerApi;
import com.homegarden.store.backend.converter.CategoryConverter;
import com.homegarden.store.backend.dto.CategoryDto;
import com.homegarden.store.backend.entity.Category;
import com.homegarden.store.backend.service.CategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CategoryController implements CategoryControllerApi {

    private final CategoryService categoryService;
    private final CategoryConverter converter;

    @Override
    public ResponseEntity<CategoryDto> create(@Valid CategoryDto dto) {
        Category created = categoryService.create(converter.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(converter.toDto(created));
    }

    @Override
    public ResponseEntity<List<CategoryDto>> getAll() {
        List<CategoryDto> result = categoryService.getAll().stream()
                .map(converter::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<CategoryDto> getById(@Min(1) Long categoryId) {
        return ResponseEntity.ok(converter.toDto(categoryService.getById(categoryId)));
    }

    @Override
    public ResponseEntity<Void> delete(@Min(1) Long categoryId) {
        categoryService.delete(categoryId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<CategoryDto> updateCategory(@Min(1) Long categoryId, @Valid CategoryDto dto) {
        Category updated = categoryService.update(categoryId, dto.name());
        return ResponseEntity.ok(converter.toDto(updated));
    }
}
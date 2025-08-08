package com.homegarden.store.backend.controller;

import com.homegarden.store.backend.converter.Converter;
import com.homegarden.store.backend.dto.CategoryDto;
import com.homegarden.store.backend.entity.Category;
import com.homegarden.store.backend.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final Converter<Category, CategoryDto, CategoryDto> converter;

    @PostMapping
    public ResponseEntity<CategoryDto> create(@RequestBody @Valid CategoryDto dto) {
        Category category = categoryService.create(converter.toEntity(dto));

        return ResponseEntity.status(HttpStatus.CREATED).body(converter.toDto(category));
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAll() {
        List<CategoryDto> response = categoryService
                .getAll()
                .stream()
                .map(converter::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(converter.toDto(categoryService.getById(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable("id") Long id,
            @RequestBody @Valid CategoryDto dto) {
        Category updated = categoryService.update(id, dto.name());

        return ResponseEntity.ok(converter.toDto(updated));
    }
}
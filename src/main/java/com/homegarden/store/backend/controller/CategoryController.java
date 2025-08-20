package com.homegarden.store.backend.controller;

import com.homegarden.store.backend.controller.api.CategoryControllerApi;
import com.homegarden.store.backend.converter.Converter;
import com.homegarden.store.backend.dto.CategoryDto;
import com.homegarden.store.backend.entity.Category;
import com.homegarden.store.backend.service.CategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/categories")
@RequiredArgsConstructor
@Validated
public class CategoryController implements CategoryControllerApi {

    private final CategoryService categoryService;
    private final Converter<Category, CategoryDto, CategoryDto> converter;

    @Override
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PostMapping
    public ResponseEntity<CategoryDto> create(@RequestBody @Valid CategoryDto dto) {
        Category saved = categoryService.create(converter.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(converter.toDto(saved));
    }

    @Override
    @PreAuthorize("permitAll()")
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAll() {
        List<CategoryDto> result = categoryService.getAll()
                .stream()
                .map(converter::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @Override
    @PreAuthorize("permitAll()")
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getById(@PathVariable("categoryId") @Min(1) Long categoryId) {
        return ResponseEntity.ok(converter.toDto(categoryService.getById(categoryId)));
    }

    @Override
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> delete(@PathVariable("categoryId") @Min(1) Long categoryId) {
        categoryService.delete(categoryId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable("categoryId") @Min(1) Long categoryId,
                                                      @RequestBody @Valid CategoryDto dto) {
        Category updated = categoryService.update(categoryId, dto.name());
        return ResponseEntity.ok(converter.toDto(updated));
    }
}
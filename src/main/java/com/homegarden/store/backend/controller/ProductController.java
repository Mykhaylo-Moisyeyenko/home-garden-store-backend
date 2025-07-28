package com.homegarden.store.backend.controller;

import com.homegarden.store.backend.converter.Converter;
import com.homegarden.store.backend.dto.CreateProductDto;
import com.homegarden.store.backend.dto.ProductDto;
import com.homegarden.store.backend.entity.Product;
import com.homegarden.store.backend.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final Converter<Product, CreateProductDto, ProductDto> converter;

    @PostMapping
    public ResponseEntity<ProductDto> create(@RequestBody @Valid CreateProductDto productDto) {
        Product product = converter.toEntity(productDto);
        product.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        product.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        Product created = productService.create(product);
        return ResponseEntity.status(201).body(converter.toDto(created));
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAll(
            @RequestParam(required = false) @Min(1) Long categoryId,
            @RequestParam(required = false) @PositiveOrZero BigDecimal minPrice,
            @RequestParam(required = false) @Positive BigDecimal maxPrice,
            @RequestParam(required = false) Boolean discount,
            @RequestParam(required = false) @Pattern(regexp = "ASC|DESC") String sort
    ) {
        List<ProductDto> dtos = productService.getAll(categoryId, minPrice, maxPrice, discount, sort).stream()
                .map(converter::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getById(@PathVariable @Min(1) Long id) {
        Product product = productService.getById(id);
        return ResponseEntity.ok(converter.toDto(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Min(1) Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable("id") Long id,
            @RequestBody @Valid CreateProductDto productDto) {
        Product productToUpdate = converter.toEntity(productDto);
        productToUpdate.setProductId(id);
        productToUpdate.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        Product updated = productService.update(productToUpdate);
        return ResponseEntity.ok(converter.toDto(updated));
    }
}
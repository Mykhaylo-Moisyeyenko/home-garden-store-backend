package com.homegarden.store.backend.controller;

import com.homegarden.store.backend.converter.Converter;
import com.homegarden.store.backend.converter.ProductConverter;
import com.homegarden.store.backend.model.dto.CreateProductDto;
import com.homegarden.store.backend.model.dto.ProductDto;
import com.homegarden.store.backend.model.entity.Product;
import com.homegarden.store.backend.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/products")
@RequiredArgsConstructor
@Validated
public class ProductController {

    private final ProductService productService;
    private final ProductConverter productConverter; // ✅ Инжектируем интерфейс!

    @PostMapping
    public ResponseEntity<ProductDto> create(@RequestBody @Valid CreateProductDto productDto) {
        Product product = productConverter.toEntity(productDto);
        Product created = productService.create(product);
        return ResponseEntity.ok(productConverter.toDto(created));
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAll() {
        List<ProductDto> dtos = productService.getAll().stream()
                .map(productConverter::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getById(@PathVariable Long id) {
        Product product = productService.getById(id);
        return ResponseEntity.ok(productConverter.toDto(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable("id") Long id,
            @RequestBody @Valid CreateProductDto productDto) {
        Product productToUpdate = productConverter.toEntity(productDto);
        productToUpdate.setProductId(id); // ✅ Теперь должно работать, если productId в Product — Long
        Product updated = productService.update(productToUpdate);
        return ResponseEntity.ok(productConverter.toDto(updated));
    }
}
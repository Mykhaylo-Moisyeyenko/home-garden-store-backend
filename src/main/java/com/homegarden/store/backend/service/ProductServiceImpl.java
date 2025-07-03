package com.homegarden.store.backend.service;

import com.homegarden.store.backend.converter.ProductConverter;
import com.homegarden.store.backend.model.dto.ProductDto;
import com.homegarden.store.backend.model.entity.Product;
import com.homegarden.store.backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl extends ProductService {

    private final ProductRepository productRepository;
    private final ProductConverter productConverter;

    @Override
    public ProductDto create(ProductDto productDto) {
        Product product = productConverter.toEntity(productDto);
        Product saved = productRepository.save(product);
        return productConverter.toDto(saved);
    }

    @Override
    public List<ProductDto> getAll() {
        return productRepository.findAll().stream()
                .map(productConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDto getById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return productConverter.toDto(product);
    }

    @Override
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }
}


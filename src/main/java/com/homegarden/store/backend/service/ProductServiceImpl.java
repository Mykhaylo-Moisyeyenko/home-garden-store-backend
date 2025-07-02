package com.homegarden.store.backend.service;

import com.homegarden.store.backend.exception.ProductNotFoundException;
import com.homegarden.store.backend.mapper.ProductMapper;
import com.homegarden.store.backend.model.dto.ProductDto;
import com.homegarden.store.backend.model.entity.Category;
import com.homegarden.store.backend.model.entity.Product;
import com.homegarden.store.backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;



    @Override

    public ProductDto create(ProductDto productDto) {
        Product product = productMapper.toEntity(productDto);

        // Загружаем категорию из БД
        Category category = (Category) productRepository.findById(productDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        product.setCategory(category);

        Product saved = productRepository.save(product);
        return productMapper.toDto(saved);
    }

    @Override
    public List<ProductDto> getAll() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDto getById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        return productMapper.toDto(product);

    }

    @Override
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);

    }
}

package com.homegarden.store.backend.service;

import com.homegarden.store.backend.exception.ProductNotFoundException;
import com.homegarden.store.backend.model.entity.Product;
import com.homegarden.store.backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Product create(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    }

    @Override
    public void delete(Long id) {
        getById(id);
        productRepository.deleteById(id);
    }

    @Override
    public Product update(Product product) {
        Long id = product.getProductId();

        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));

        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());

        if (product.getCategory() != null) {
            existing.setCategory(product.getCategory()); // обновлена категория
        }

        existing.setImageUrl(product.getImageUrl());
        existing.setDiscountPrice(product.getDiscountPrice());
        existing.setUpdatedAt(product.getUpdatedAt());

        return productRepository.save(existing);
    }
}

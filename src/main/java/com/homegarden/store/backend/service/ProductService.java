package com.homegarden.store.backend.service;

import com.homegarden.store.backend.entity.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    Product create(Product product);

    List<Product> getAll(
            Long categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean discount,
            String sort);

    Product getById(Long id);

    void delete(Long id);

    Product update(Product product);

    boolean existsById(Long id);
}
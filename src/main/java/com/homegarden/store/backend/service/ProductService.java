package com.homegarden.store.backend.service;

import com.homegarden.store.backend.model.entity.Product;

import java.util.List;

public interface ProductService {

    Product create(Product product);

    List<Product> getAll();

    Product getById(Long id);

    void delete(Long id);

    Product update(Product product);
}
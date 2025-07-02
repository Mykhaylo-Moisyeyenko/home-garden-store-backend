package com.homegarden.store.backend.service;

import com.homegarden.store.backend.model.dto.ProductDto;

import java.util.List;

public interface ProductService {
    ProductDto create(ProductDto productDto);


    List<ProductDto> getAll();


    ProductDto getById(Long id);


    void delete(Long id);

}


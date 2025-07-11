package com.homegarden.store.backend.service;

import com.homegarden.store.backend.model.dto.CreateCartRequestDTO;
import com.homegarden.store.backend.model.entity.Cart;
import jakarta.validation.Valid;

import java.util.List;

public interface CartService {
    Cart create(@Valid CreateCartRequestDTO userId);
    Cart getById(Long id);
    List<Cart> getAll();
    void delete(Long id);
}


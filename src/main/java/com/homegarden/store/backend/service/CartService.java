package com.homegarden.store.backend.service;

import com.homegarden.store.backend.entity.Cart;

import java.util.List;

public interface CartService {
    Cart create(Cart cart);
    Cart getById(Long id);

    boolean existsByUserId(Long userId);

    List<Cart> getAll();
    void delete(Long id);
}

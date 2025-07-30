package com.homegarden.store.backend.service;

import com.homegarden.store.backend.entity.CartItem;

import java.util.List;
import java.util.Optional;

public interface CartItemService {
    CartItem create(CartItem cartItem);

    Optional<CartItem> updateQuantity(Long id, Integer quantity);

    CartItem getById(Long id);

    List<CartItem> getAll();

    void delete(Long id);
}
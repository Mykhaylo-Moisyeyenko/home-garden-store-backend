package com.homegarden.store.backend.service;

import com.homegarden.store.backend.dto.CreateCartItemRequestDTO;
import com.homegarden.store.backend.entity.CartItem;

import java.util.List;

public interface CartItemService {
    CartItem create(CartItem cartItem);
    CartItem updateQuantity(Long id, Integer quantity);
    CartItem getById(Long id);
    List<CartItem> getAll();
    void delete(Long id);
}

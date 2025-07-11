package com.homegarden.store.backend.service;

import com.homegarden.store.backend.model.dto.CreateCartItemRequestDTO;
import com.homegarden.store.backend.model.entity.CartItem;

import java.util.List;

public interface CartItemService {
    CartItem create(CreateCartItemRequestDTO dto);
    CartItem updateQuantity(Long id, Integer quantity);
    CartItem getById(Long id);
    List<CartItem> getAll();
    void delete(Long id);
}

package com.homegarden.store.backend.service;

import com.homegarden.store.backend.entity.Cart;
import com.homegarden.store.backend.entity.CartItem;
import com.homegarden.store.backend.entity.User;

import java.util.List;

public interface CartService {

    Cart create(Cart cart);

    List<CartItem> getAllCartItems();

    void delete();

    Cart update(Cart cart);

    Cart addCartItem(CartItem cartItem);

    Cart updateCartItemQuantity(Long id, Integer quantity);

    Cart deleteCartItem(Long id);

    Cart getByUser(User user);
}
package com.homegarden.store.backend.service;

import com.homegarden.store.backend.entity.Cart;
import com.homegarden.store.backend.entity.CartItem;
import com.homegarden.store.backend.entity.User;
import com.homegarden.store.backend.exception.CartAlreadyExistsException;
import com.homegarden.store.backend.exception.CartItemNotFoundException;
import com.homegarden.store.backend.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserService userService;

    @Override
    public Cart create(Cart cart) {
        User user = userService.getCurrentUser();
        Cart existing = cartRepository.getByUser(user);
        if (existing != null) {
            throw new CartAlreadyExistsException("Cart already exists for this user");
        }
        cart.setUser(user);
        return cartRepository.save(cart);
    }

    @Override
    public List<CartItem> getAllCartItems() {
        User user = userService.getCurrentUser();
        Cart cart = cartRepository.getByUser(user);
        return cart != null ? cart.getItems() : List.of();
    }

    @Override
    public void delete() {
        User user = userService.getCurrentUser();
        Cart cart = cartRepository.getByUser(user);
        if (cart != null) {
            cartRepository.delete(cart);
        }
    }

    @Override
    public Cart getByUser(User user) {
        return cartRepository.getByUser(user);
    }

    @Override
    public Cart update(Cart cart) {
        Cart updatedCart = userService.getCurrentUser().getCart();
        updatedCart.setItems(cart.getItems());
        return cartRepository.save(updatedCart);
    }

    @Override
    public Cart addCartItem(CartItem cartItem) {
        User user = userService.getCurrentUser();
        Cart cart = cartRepository.getByUser(user);
        if (cart == null) {

            cart = Cart.builder()
                    .user(user)
                    .items(new ArrayList<>())
                    .build();
        }

        cart.getItems().add(cartItem);
        return cartRepository.save(cart);
    }

    @Override
    public Cart updateCartItemQuantity(Long id, Integer quantity) {
        Cart cart = userService.getCurrentUser().getCart();
        CartItem item = findCartItem(id);
        item.setQuantity(quantity);
        if (quantity.equals(0)) {
            cart.getItems().remove(item);
        }
        return cartRepository.save(cart);
    }

    @Override
    public Cart deleteCartItem(Long id) {
        findCartItem(id);
        return updateCartItemQuantity(id, 0);
    }

    private CartItem findCartItem(Long id) {
        Cart cart = userService.getCurrentUser().getCart();
        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new CartItemNotFoundException("Cart Item is not found");
        }
        return cart.getItems().stream()
                .filter((cartItem) -> cartItem.getCartItemId().equals(id))
                .findFirst()
                .orElseThrow(() -> new CartItemNotFoundException("Cart Item is not found"));
    }
}
package com.homegarden.store.backend.service;

import com.homegarden.store.backend.entity.Cart;
import com.homegarden.store.backend.entity.CartItem;
import com.homegarden.store.backend.entity.User;
import com.homegarden.store.backend.exception.CartAlreadyExistsException;
import com.homegarden.store.backend.exception.CartItemNotFoundException;
import com.homegarden.store.backend.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserService userService;

    private final AccessCheckService accessCheckService;

    @Override
    public Cart create(Cart cart) {
        User user = userService.getById(cart.getUser().getUserId());
        accessCheckService.checkAccess(user);
        if (cartRepository.existsCartByUser(user)) {
            throw new CartAlreadyExistsException("Cart already exists for this user");
        }
        cart.setUser(user);
        return cartRepository.save(cart);
    }

    @Override
    public List<CartItem> getAllCartItems() {
        User user = userService.getByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        Cart cart = cartRepository.getByUser(user);
        return cart.getItems();
    }

    @Override
    public void delete(Long id) {
        Cart cart = getCart();
        accessCheckService.checkAccess(cart);
        cartRepository.deleteById(id);
    }

    @Override
    public Cart getByUser(User user) {
        return cartRepository.getByUser(user);
    }

    @Override
    public Cart update(Cart cart) {
        Cart updatedCart = getCart();
        updatedCart.setItems(cart.getItems());
        return cartRepository.save(updatedCart);
    }

    @Override
    public Cart addCartItem(CartItem cartItem) {
        Cart cart = getCart();
        cart.getItems().add(cartItem);
        return cartRepository.save(cart);
    }

    @Override
    public Cart updateCartItemQuantity(Long id, Integer quantity) {
        Cart cart = getCart();
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

    private Cart getCart() {
        User user = userService.getByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        return cartRepository.getByUser(user);
    }

    private CartItem findCartItem(Long id) {
        Cart cart = getCart();
        List<CartItem> item = cart.getItems().stream().filter((cartItem) -> cartItem.getCartItemId().equals(id)).toList();
        if (item.isEmpty()) {
            throw new CartItemNotFoundException("Cart Item is not found");
        }
        return item.get(0);
    }
}
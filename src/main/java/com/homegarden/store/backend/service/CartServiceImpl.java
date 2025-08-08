package com.homegarden.store.backend.service;

import com.homegarden.store.backend.entity.Cart;
import com.homegarden.store.backend.entity.CartItem;
import com.homegarden.store.backend.entity.User;
import com.homegarden.store.backend.exception.CartAlreadyExistsException;
import com.homegarden.store.backend.exception.CartNotFoundException;
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

    @Override
    public Cart create(Cart cart) {
        User user = userService.getById(cart.getUser().getUserId());
        if (cartRepository.existsCartByUser(user)) {
            throw new CartAlreadyExistsException("Cart already exists for this user");
        }
        cart.setUser(user);
        return cartRepository.save(cart);
    }

    @Override
    public Cart getById(Long id) {
        return cartRepository.findById(id).orElseThrow(() -> new CartNotFoundException("Cart not found"));
    }

    @Override
    public List<CartItem> getAllCartItems() {
        User user = userService.getByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        Cart cart = cartRepository.getByUser(user);
        return cart.getItems();
    }

    @Override
    public void delete(Long id) {
        getById(id);
        cartRepository.deleteById(id);
    }

    @Override
    public Cart getByUser(User user) {
        return cartRepository.getByUser(user);
    }

    @Override
    public Cart update(Cart cart) {
        Cart updatedCart = getById(cart.getCartId());
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

        CartItem item = cart.getItems().stream()
                .filter((cartItem) -> cartItem.getCartItemId().equals(id))
                .toList().getFirst();

        item.setQuantity(quantity);
        if (quantity.equals(0)) {
            cart.getItems().remove(item);
        }
        return cartRepository.save(cart);
    }

    @Override
    public Cart deleteCartItem(Long id) {
        return updateCartItemQuantity(id, 0);
    }

    private Cart getCart() {
        User user = userService.getByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        return cartRepository.getByUser(user);
    }
}
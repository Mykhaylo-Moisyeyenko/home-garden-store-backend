package com.homegarden.store.backend.service;

import com.homegarden.store.backend.entity.Cart;
import com.homegarden.store.backend.entity.User;
import com.homegarden.store.backend.exception.CartAlreadyExistsException;
import com.homegarden.store.backend.exception.CartNotFoundException;
import com.homegarden.store.backend.repository.CartRepository;
import lombok.RequiredArgsConstructor;
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
        Long userId = cart.getUser().getUserId();
        User user = userService.getById(userId);
        accessCheckService.checkAccess(user);
        if(cartRepository.existsCartByUser(user)) {
            throw new CartAlreadyExistsException("Cart already exists for this user");
        }
        cart.setUser(user);
        return cartRepository.save(cart);
    }

    @Override
    public Cart getById(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));
        accessCheckService.checkAccess(cart);
        return cart;
    }

    @Override
    public List<Cart> getAll() {
        return cartRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        Cart cart = getById(id);
        accessCheckService.checkAccess(cart);
        cartRepository.deleteById(id);
    }

    @Override
    public Cart getByUserId(Long userId) {
        List<Cart> cart = cartRepository.findByUser_UserId(userId);
        if (cart.isEmpty()) {
            throw new CartNotFoundException("Cart for User " + userId + " not found");
        }
        return cart.get(0);
    }

    @Override
    public Cart update(Cart cart) {
        Cart updatedCart = getById(cart.getCartId());
        updatedCart.setItems(cart.getItems());
        return cartRepository.save(updatedCart);
    }
}
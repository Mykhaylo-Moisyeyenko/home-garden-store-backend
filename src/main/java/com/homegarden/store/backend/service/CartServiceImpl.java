package com.homegarden.store.backend.service;

import com.homegarden.store.backend.dto.CreateCartRequestDTO;
import com.homegarden.store.backend.entity.Cart;
import com.homegarden.store.backend.entity.User;
import com.homegarden.store.backend.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final UserService userService;

    @Override
    public Cart create(Cart cart) {
        Long userId = cart.getUser().getUserId();
        User user = userService.getById(userId);
        cart.setUser(user);
        return cartRepository.save(cart);
    }

    @Override
    public Cart getById(Long id) {
        return cartRepository.findById(id).orElseThrow(() -> new RuntimeException("Cart not found"));
        //здесь нужно выбросить пользовательское исключение CartNotFoundException
    }

    @Override
    public List<Cart> getAll() {
        return cartRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        cartRepository.deleteById(id);
    }
}

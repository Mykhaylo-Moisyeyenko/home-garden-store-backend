package com.homegarden.store.backend.service;

import com.homegarden.store.backend.model.dto.CreateCartRequestDTO;
import com.homegarden.store.backend.model.entity.Cart;
import com.homegarden.store.backend.model.entity.User;
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
    public Cart create(CreateCartRequestDTO dto) {
        User user = userService.getById(dto.userId());
        Cart cart = Cart.builder().user(user).build();
        return cartRepository.save(cart);
    }

    @Override
    public Cart getById(Long id) {
        return cartRepository.findById(id).orElseThrow(() -> new RuntimeException("Cart not found"));
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

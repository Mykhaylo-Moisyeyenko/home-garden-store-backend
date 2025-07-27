package com.homegarden.store.backend.service;

import com.homegarden.store.backend.entity.Cart;
import com.homegarden.store.backend.entity.CartItem;
import com.homegarden.store.backend.entity.Product;
import com.homegarden.store.backend.exception.CartItemNotFoundException;
import com.homegarden.store.backend.repository.CartItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartService cartService;
    private final ProductService productService;

    @Override
    public CartItem create(CartItem item) {
        Long cartId = item.getCart().getCartId();
        Long productId = item.getProduct().getProductId();

        Cart cart = cartService.getById(cartId);
        Product product = productService.getById(productId);

        item.setCart(cart);
        item.setProduct(product);

        return cartItemRepository.save(item);
    }

    @Override
    public CartItem updateQuantity(Long id, Integer quantity) {
        CartItem item = getById(id);

        item.setQuantity(quantity);
        return cartItemRepository.save(item);
    }

    @Override
    public CartItem getById(Long id) {
        return cartItemRepository.findById(id)
                .orElseThrow(() -> new CartItemNotFoundException("Cart item not found"));
    }

    @Override
    public List<CartItem> getAll() {
        return cartItemRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        getById(id);
        cartItemRepository.deleteById(id);
    }
}
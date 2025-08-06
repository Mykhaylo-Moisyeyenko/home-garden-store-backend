package com.homegarden.store.backend.service;

import com.homegarden.store.backend.entity.Cart;
import com.homegarden.store.backend.entity.CartItem;
import com.homegarden.store.backend.entity.Product;
import com.homegarden.store.backend.exception.CartItemNotFoundException;
import com.homegarden.store.backend.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public Optional<CartItem> updateQuantity(Long id, Integer quantity) {
        CartItem item = getById(id);

        item.setQuantity(quantity);
        if (quantity.equals(0)) {
            cartItemRepository.delete(item);
            return Optional.empty();
        } else {
            return Optional.of(cartItemRepository.save(item));
        }
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
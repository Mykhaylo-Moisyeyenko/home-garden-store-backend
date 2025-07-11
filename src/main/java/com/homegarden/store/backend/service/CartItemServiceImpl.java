package com.homegarden.store.backend.service;

import com.homegarden.store.backend.model.dto.CreateCartItemRequestDTO;
import com.homegarden.store.backend.model.entity.Cart;
import com.homegarden.store.backend.model.entity.CartItem;
import com.homegarden.store.backend.model.entity.Product;
import com.homegarden.store.backend.repository.CartItemRepository;
import com.homegarden.store.backend.repository.CartRepository;
import com.homegarden.store.backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Override
    public CartItem create(CartItem item) {
        Long cartId = item.getCart().getCartId();
        Long productId = item.getProduct().getProductId();

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        item.setCart(cart);
        item.setProduct(product);
        item.setPrice(BigDecimal.valueOf(product.getPrice()));

        return cartItemRepository.save(item);
    }

    @Override
    public CartItem updateQuantity(Long id, Integer quantity) {
        CartItem item = cartItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        item.setQuantity(quantity);
        return cartItemRepository.save(item);
    }

    @Override
    public CartItem getById(Long id) {
        return cartItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
    }

    @Override
    public List<CartItem> getAll() {
        return cartItemRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        cartItemRepository.deleteById(id);
    }
}

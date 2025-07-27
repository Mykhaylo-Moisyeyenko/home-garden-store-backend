package com.homegarden.store.backend.converter;

import com.homegarden.store.backend.dto.CartItemResponseDTO;
import com.homegarden.store.backend.dto.CreateCartItemRequestDTO;
import com.homegarden.store.backend.entity.Cart;
import com.homegarden.store.backend.entity.CartItem;
import com.homegarden.store.backend.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class CartItemConverter implements Converter<CartItem, CreateCartItemRequestDTO, CartItemResponseDTO> {

    @Override
    public CartItem toEntity(CreateCartItemRequestDTO dto) {
        Cart cart = new Cart();
        cart.setCartId(dto.cartId());

        Product product = new Product();
        product.setProductId(dto.productId());

        return CartItem.builder()
                .cart(cart)
                .product(product)
                .quantity(dto.quantity())
                .build();
    }

    @Override
    public CartItemResponseDTO toDto(CartItem item) {
        return new CartItemResponseDTO(
                item.getId(),
                item.getCart().getCartId(),
                item.getProduct().getProductId(),
                item.getProduct().getName(),
                item.getQuantity());
    }
}

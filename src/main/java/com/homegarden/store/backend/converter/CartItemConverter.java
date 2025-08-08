package com.homegarden.store.backend.converter;

import com.homegarden.store.backend.dto.CartItemResponseDto;
import com.homegarden.store.backend.dto.CreateCartItemRequestDto;
import com.homegarden.store.backend.entity.Cart;
import com.homegarden.store.backend.entity.CartItem;
import com.homegarden.store.backend.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class CartItemConverter implements Converter<CartItem, CreateCartItemRequestDto, CartItemResponseDto> {

    @Override
    public CartItem toEntity(CreateCartItemRequestDto dto) {
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
    public CartItemResponseDto toDto(CartItem item) {

        return CartItemResponseDto.builder()
                .id(item.getCartItemId())
                .cartId(item.getCartItemId())
                .productId(item.getProduct().getProductId())
                .productName(item.getProduct().getName())
                .quantity(item.getQuantity())
                .build();
    }
}

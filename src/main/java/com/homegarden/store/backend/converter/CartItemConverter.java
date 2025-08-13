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
        Product product = new Product();
        product.setProductId(dto.productId());

        return CartItem.builder()
                .product(product)
                .quantity(dto.quantity())
                .build();
    }

    @Override
    public CartItemResponseDto toDto(CartItem item) {

        return CartItemResponseDto.builder()
                .productId(item.getProduct().getProductId())
                .productName(item.getProduct().getName())
                .quantity(item.getQuantity())
                .build();
    }
}

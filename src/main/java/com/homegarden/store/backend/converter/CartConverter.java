package com.homegarden.store.backend.converter;

import com.homegarden.store.backend.dto.CartResponseDto;
import com.homegarden.store.backend.entity.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartConverter implements Converter<Cart, Object, CartResponseDto> {

    private final CartItemConverter converter;

    @Override
    public CartResponseDto toDto(Cart cart) {
        return new CartResponseDto(
                cart.getCartId(),
                cart.getUser().getUserId(),
                cart.getItems().stream()
                        .map(converter::toDto)
                        .toList());
    }

    @Override
    public Cart toEntity(Object dto) {
        throw new UnsupportedOperationException("Conversion from DTO to Cart is not supported anymore.");
    }
}
package com.homegarden.store.backend.converter;

import com.homegarden.store.backend.dto.CartResponseDto;
import com.homegarden.store.backend.dto.CreateCartItemRequestDto;
import com.homegarden.store.backend.entity.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartConverter implements Converter<Cart, CreateCartItemRequestDto, CartResponseDto> {

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
    public Cart toEntity(CreateCartItemRequestDto dto) {
        throw new UnsupportedOperationException("Conversion from DTO to Cart is not supported anymore.");
    }
}
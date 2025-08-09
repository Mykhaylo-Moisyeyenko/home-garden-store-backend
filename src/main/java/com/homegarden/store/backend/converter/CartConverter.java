package com.homegarden.store.backend.converter;

import com.homegarden.store.backend.dto.CartResponseDto;
import com.homegarden.store.backend.dto.CreateCartRequestDto;
import com.homegarden.store.backend.entity.Cart;
import com.homegarden.store.backend.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartConverter implements Converter<Cart, CreateCartRequestDto, CartResponseDto> {

    private final CartItemConverter converter;

    @Override
    public Cart toEntity(CreateCartRequestDto dto) {
        return Cart.builder()
                .user(User
                        .builder()
                        .userId(dto.userId())
                        .build())
                .build();
    }

    @Override
    public CartResponseDto toDto(Cart cart) {
        return new CartResponseDto(
                cart.getCartId(),
                cart.getUser().getUserId(),
                cart.getItems().stream()
                        .map(converter::toDto)
                        .toList());
    }
}
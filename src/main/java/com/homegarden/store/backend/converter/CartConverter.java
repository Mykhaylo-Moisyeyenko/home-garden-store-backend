package com.homegarden.store.backend.converter;

import com.homegarden.store.backend.dto.CartResponseDto;
import com.homegarden.store.backend.dto.CreateCartRequestDto;
import com.homegarden.store.backend.entity.Cart;
import com.homegarden.store.backend.entity.User;
import org.springframework.stereotype.Component;

@Component

public class CartConverter implements Converter<Cart, CreateCartRequestDto, CartResponseDto> {

    @Override
    public Cart toEntity(CreateCartRequestDto dto) {

        User user = User
                .builder()
                .userId(dto.userId())
                .build();

        return Cart
                .builder()
                .user(user)
                .build();
    }

    @Override
    public CartResponseDto toDto(Cart cart) {

        return new CartResponseDto(cart.getCartId(), cart.getUser().getUserId());
    }
}

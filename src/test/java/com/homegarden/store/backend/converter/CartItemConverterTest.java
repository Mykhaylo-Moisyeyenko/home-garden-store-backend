package com.homegarden.store.backend.converter;

import com.homegarden.store.backend.dto.CartResponseDto;
import com.homegarden.store.backend.entity.Cart;
import com.homegarden.store.backend.entity.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CartConverterTest {

    private final CartItemConverter cartItemConverter = new CartItemConverter();
    private final CartConverter cartConverter = new CartConverter(cartItemConverter);

    @Test
    void toDtoTest() {
        Cart cart = new Cart(
                1L,
                new ArrayList<>(),
                User.builder().userId(1L).build());

        CartResponseDto actual = cartConverter.toDto(cart);

        assertEquals(1L, actual.cartId());
        assertEquals(1L, actual.userId());
        assertEquals(0, actual.items().size());
    }
}
package com.homegarden.store.backend.converter;

import com.homegarden.store.backend.dto.CartResponseDto;
import com.homegarden.store.backend.dto.CreateCartRequestDto;
import com.homegarden.store.backend.entity.Cart;
import com.homegarden.store.backend.entity.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CartConverterTest {

    private CartConverter cartConverter = new CartConverter();

    CreateCartRequestDto createCartRequestDTO = new CreateCartRequestDto(1L);

    Cart cart = new Cart(1L, new ArrayList<>(), User.builder().userId(1L).build());

    @Test
    void toEntityTest() {
        Cart actual = cartConverter.toEntity(createCartRequestDTO);

        assertThat(actual).isNotNull();
        assertThat(actual.getUser().getUserId()).isEqualTo(1L);
    }

    @Test
    void toDtoTest() {
        CartResponseDto actual = cartConverter.toDto(cart);

        assertEquals(1L, actual.cartId());
        assertEquals(1L, actual.userId());
    }
}
package com.homegarden.store.backend.converter;

import com.homegarden.store.backend.dto.CartResponseDto;
import com.homegarden.store.backend.dto.CreateCartItemRequestDto;
import com.homegarden.store.backend.entity.Cart;
import com.homegarden.store.backend.entity.CartItem;
import com.homegarden.store.backend.entity.Product;
import com.homegarden.store.backend.entity.User;
import com.homegarden.store.backend.enums.Role;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CartConverterTest {

    private final CartItemConverter cartItemConverter = new CartItemConverter();
    private final CartConverter cartConverter = new CartConverter(cartItemConverter);

    User user = User.builder()
            .userId(1L)
            .name("Misha")
            .email("misha@gmail.com")
            .phoneNumber("+11111111111")
            .passwordHash("123456")
            .role(Role.ROLE_USER)
            .build();

    CartItem cartItem1 = CartItem.builder()
            .cartItemId(1L)
            .product(Product.builder().productId(1L).build())
            .quantity(10)
            .build();

    CartItem cartItem2 = CartItem.builder()
            .cartItemId(2L)
            .product(Product.builder().productId(2L).build())
            .quantity(20)
            .build();

    Cart cart = Cart.builder()
            .cartId(1L)
            .user(user)
            .items(List.of(cartItem1, cartItem2))
            .build();

    @Test
    void toDtoTest() {
        CartResponseDto actual = cartConverter.toDto(cart);

        assertEquals(1L, actual.cartId());
        assertEquals(1L, actual.userId());
        assertEquals(2, actual.items().size());
    }

    @Test
    void toEntityTest() {
        assertThrows(UnsupportedOperationException.class,
                () -> cartConverter.toEntity(new CreateCartItemRequestDto(1L, 10)));
    }
}
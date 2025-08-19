package com.homegarden.store.backend.converter;

import com.homegarden.store.backend.dto.CartItemResponseDto;
import com.homegarden.store.backend.dto.CreateCartItemRequestDto;
import com.homegarden.store.backend.entity.CartItem;
import com.homegarden.store.backend.entity.Product;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CartItemConverterTest {

    private final CartItemConverter cartItemConverter = new CartItemConverter();

    CartItem cartItem1 = CartItem.builder()
            .product(Product.builder()
                    .productId(1L)
                    .build())
            .quantity(10)
            .build();

    CartItem cartItem2 = CartItem.builder()
            .cartItemId(2L)
            .product(Product.builder()
                    .productId(2L)
                    .name("Pillow")
                    .build())
            .quantity(20)
            .build();

    CreateCartItemRequestDto dto = new CreateCartItemRequestDto(1L, 10);

    @Test
    void toEntityTest() {
        CartItem actual = cartItemConverter.toEntity(dto);

        assertEquals(cartItem1, actual);
    }

    @Test
    void toDtoTest() {
        CartItemResponseDto actual = cartItemConverter.toDto(cartItem2);

        assertEquals(2L, actual.productId());
        assertEquals("Pillow", actual.productName());
        assertEquals(20, actual.quantity());
    }
}
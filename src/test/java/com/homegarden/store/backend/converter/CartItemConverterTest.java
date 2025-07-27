package com.homegarden.store.backend.converter;

import com.homegarden.store.backend.dto.CartItemResponseDTO;
import com.homegarden.store.backend.dto.CreateCartItemRequestDTO;
import com.homegarden.store.backend.entity.Cart;
import com.homegarden.store.backend.entity.CartItem;
import com.homegarden.store.backend.entity.Product;
import com.homegarden.store.backend.entity.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CartItemConverterTest {

    private CartItemConverter converter = new CartItemConverter();

    CreateCartItemRequestDTO dto = new CreateCartItemRequestDTO(1L, 1L, 10);
    CartItemResponseDTO responseDTO = new CartItemResponseDTO(1L, 1L, 1L, "TestProduct", 10);

    Cart cart = new Cart(1L, new ArrayList<>(), new User());
    Product product = Product.builder().productId(1L).name("TestProduct").build();
    CartItem cartItem = new CartItem(1L, cart, product, 10);


    @Test
    void toEntityTest() {
        CartItem actual = converter.toEntity(dto);

        assertEquals(cart, actual.getCart());
        assertEquals(product, actual.getProduct());
        assertEquals(10, actual.getQuantity());
    }

    @Test
    void toDtoTest() {
        CartItemResponseDTO actual = converter.toDto(cartItem);

        assertEquals(responseDTO, actual);
        assertEquals(1L, actual.id());
        assertEquals(1L, actual.cartId());
        assertEquals(1L, actual.productId());
        assertEquals(10, actual.quantity());
    }
}
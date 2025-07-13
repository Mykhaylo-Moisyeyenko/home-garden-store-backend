package com.homegarden.store.backend.converter;

import com.homegarden.store.backend.dto.CartResponseDTO;
import com.homegarden.store.backend.dto.CreateCartRequestDTO;
import com.homegarden.store.backend.entity.Cart;
import com.homegarden.store.backend.entity.User;
import org.springframework.stereotype.Component;

@Component
public class CartConverter implements Converter<Cart, CreateCartRequestDTO, CartResponseDTO> {

    @Override
    public Cart toEntity(CreateCartRequestDTO dto) {
        return Cart.builder().build();
    }

    @Override
    public CartResponseDTO toDto(Cart cart) {
        return new CartResponseDTO(cart.getCartId(), cart.getUser().getUserId());
    }
}

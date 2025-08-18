package com.homegarden.store.backend.controller;

import com.homegarden.store.backend.controller.api.CartControllerApi;
import com.homegarden.store.backend.converter.Converter;
import com.homegarden.store.backend.dto.CartItemResponseDto;
import com.homegarden.store.backend.dto.CartResponseDto;
import com.homegarden.store.backend.dto.CreateCartItemRequestDto;
import com.homegarden.store.backend.dto.UpdateCartItemRequestDto;
import com.homegarden.store.backend.entity.Cart;
import com.homegarden.store.backend.entity.CartItem;
import com.homegarden.store.backend.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor

public class CartController implements CartControllerApi {

    private final CartService cartService;
    private final Converter<Cart, Object, CartResponseDto> cartConverter;
    private final Converter<CartItem, CreateCartItemRequestDto, CartItemResponseDto> cartItemConverter;

    @Override
    public List<CartItemResponseDto> getAllCartItems() {

        return cartService.getAllCartItems()
                .stream()
                .map(cartItemConverter::toDto)
                .toList();
    }

    @Override
    public ResponseEntity<Void> delete() {
        cartService.delete();

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<CartResponseDto> addCartItem(@Valid CreateCartItemRequestDto dto) {
        CartItem entity = cartItemConverter.toEntity(dto);
        Cart updated = cartService.addCartItem(entity);

        return ResponseEntity.status(HttpStatus.CREATED).body(cartConverter.toDto(updated));
    }

    @Override
    public ResponseEntity<CartResponseDto> updateCartItemQuantity(@Valid UpdateCartItemRequestDto dto) {
        Cart updated = cartService.updateCartItemQuantity(dto.cartItemId(), dto.quantity());

        return ResponseEntity.ok(cartConverter.toDto(updated));
    }

    @Override
    public ResponseEntity<CartResponseDto> deleteCartItem(Long cartItemId) {
        Cart updated = cartService.deleteCartItem(cartItemId);

        return ResponseEntity.ok(cartConverter.toDto(updated));
    }
}

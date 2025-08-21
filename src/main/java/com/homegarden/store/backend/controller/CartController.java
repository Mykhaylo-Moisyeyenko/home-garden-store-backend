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
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/v1/carts")
@PreAuthorize("hasRole('USER')")
public class CartController implements CartControllerApi {

    private final CartService cartService;
    private final Converter<Cart, CreateCartItemRequestDto, CartResponseDto> cartConverter;
    private final Converter<CartItem, CreateCartItemRequestDto, CartItemResponseDto> cartItemConverter;

    @Override
    @GetMapping
    public List<CartItemResponseDto> getAllCartItems() {

        return cartService.getAllCartItems().stream().map(cartItemConverter::toDto).toList();
    }

    @Override
    @DeleteMapping
    public ResponseEntity<Void> delete() {
        cartService.delete();

        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping("/item")
    public ResponseEntity<CartResponseDto> addCartItem(@RequestBody @Valid CreateCartItemRequestDto dto) {
        CartItem entity = cartItemConverter.toEntity(dto);
        Cart updated = cartService.addCartItem(entity);

        return ResponseEntity.status(HttpStatus.CREATED).body(cartConverter.toDto(updated));
    }

    @Override
    @PutMapping("/item")
    public ResponseEntity<CartResponseDto> updateCartItemQuantity(@RequestBody @Valid UpdateCartItemRequestDto dto) {
        Cart updated = cartService.updateCartItemQuantity(dto.cartItemId(), dto.quantity());

        return ResponseEntity.ok(cartConverter.toDto(updated));
    }

    @Override
    @DeleteMapping("/item/{id}")
    public ResponseEntity<CartResponseDto> deleteCartItem(@PathVariable("id") @Min(1) Long cartItemId) {
        Cart updated = cartService.deleteCartItem(cartItemId);

        return ResponseEntity.ok(cartConverter.toDto(updated));
    }
}
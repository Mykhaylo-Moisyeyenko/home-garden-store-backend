package com.homegarden.store.backend.controller;

import com.homegarden.store.backend.converter.Converter;
import com.homegarden.store.backend.dto.*;
import com.homegarden.store.backend.entity.Cart;
import com.homegarden.store.backend.entity.CartItem;
import com.homegarden.store.backend.service.CartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/carts")
@PreAuthorize("hasRole('ADMINISTRATOR')")
public class CartController {

    private final CartService cartService;
    private final Converter<Cart, CreateCartRequestDto, CartResponseDto> cartConverter;
    private final Converter<CartItem, CreateCartItemRequestDto, CartItemResponseDto> cartItemConverter;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CartResponseDto> create(@RequestBody @Valid CreateCartRequestDto dto) {
        Cart created = cartService.create(cartConverter.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(cartConverter.toDto(created));
    }

    @GetMapping
    public List<CartItemResponseDto> getAllCartItems() {
        return cartService.getAllCartItems()
                .stream()
                .map(cartItemConverter::toDto)
                .toList();
    }

    @DeleteMapping()
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> delete() {
        cartService.delete();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/item")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CartResponseDto> addCartItem(@RequestBody @Valid CreateCartItemRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cartConverter.toDto(cartService.addCartItem(cartItemConverter.toEntity(dto))));
    }

    @PutMapping("/item")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CartResponseDto> updateCartItemQuantity(@RequestBody @Valid UpdateCartItemRequestDto dto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(cartConverter.toDto(cartService.updateCartItemQuantity(dto.cartItemId(), dto.quantity())));
    }

    @DeleteMapping("/item/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CartResponseDto> deleteCartItem(@PathVariable @NotNull @Min(1) Long id) {
        Cart updatedCart = cartService.deleteCartItem(id);
        return ResponseEntity.status(HttpStatus.OK).body(cartConverter.toDto(updatedCart));
    }
}
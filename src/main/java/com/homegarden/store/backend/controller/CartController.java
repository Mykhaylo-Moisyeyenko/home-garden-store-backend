package com.homegarden.store.backend.controller;

import com.homegarden.store.backend.converter.Converter;
import com.homegarden.store.backend.dto.CartResponseDto;
import com.homegarden.store.backend.dto.CreateCartRequestDto;
import com.homegarden.store.backend.entity.Cart;
import com.homegarden.store.backend.service.CartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/carts")
public class CartController {

    private final CartService cartService;
    private final Converter<Cart, CreateCartRequestDto, CartResponseDto> cartConverter;

    @PostMapping
    public ResponseEntity<CartResponseDto> create(@RequestBody @Valid CreateCartRequestDto dto) {
        Cart created = cartService.create(cartConverter.toEntity(dto));

        return ResponseEntity.status(HttpStatus.CREATED).body(cartConverter.toDto(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartResponseDto> getById(@PathVariable @NotNull @Min(1) Long id) {
        return ResponseEntity.ok(cartConverter.toDto(cartService.getById(id)));
    }

    @GetMapping
    public List<CartResponseDto> getAll() {
        return cartService.getAll()
                .stream()
                .map(cartConverter::toDto)
                .toList();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @NotNull @Min(1) Long id) {
        cartService.delete(id);

        return ResponseEntity.noContent().build();
    }}
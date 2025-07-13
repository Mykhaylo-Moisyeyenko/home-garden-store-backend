package com.homegarden.store.backend.controller;

import com.homegarden.store.backend.converter.CartConverter;
import com.homegarden.store.backend.dto.CartResponseDTO;
import com.homegarden.store.backend.dto.CreateCartRequestDTO;
import com.homegarden.store.backend.entity.Cart;
import com.homegarden.store.backend.service.CartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
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
    private final Converter<Cart, CreateCartRequestDTO, CartResponseDTO> cartConverter;

    @PostMapping
    public ResponseEntity<CartResponseDTO> create(@RequestBody @Valid CreateCartRequestDTO dto) {
        Cart cart = cartConverter.toEntity(dto);
        Cart created = cartService.create(cart);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartConverter.toDto(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartResponseDTO> getById(@PathVariable @NotNull @Min(1) Long id) {
        return ResponseEntity.ok(cartConverter.toDto(cartService.getById(id)));
    }

    @GetMapping
    public List<CartResponseDTO> getAll() {
        return cartService.getAll().stream().map(cartConverter::toDto).toList();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @NotNull @Min(1) Long id) {
        cartService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

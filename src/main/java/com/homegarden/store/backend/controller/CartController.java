package com.homegarden.store.backend.controller;

import com.homegarden.store.backend.converter.CartConverter;
import com.homegarden.store.backend.model.dto.CartResponseDTO;
import com.homegarden.store.backend.model.dto.CreateCartRequestDTO;
import com.homegarden.store.backend.model.entity.Cart;
import com.homegarden.store.backend.service.CartService;
import jakarta.validation.Valid;
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
    private final CartConverter cartConverter;

    @PostMapping
    public ResponseEntity<CartResponseDTO> create(@RequestBody @Valid CreateCartRequestDTO dto) {
        Cart created = cartService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartConverter.toDto(created));
    }


    @GetMapping("/{id}")
    public ResponseEntity<CartResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(cartConverter.toDto(cartService.getById(id)));
    }

    @GetMapping
    public List<CartResponseDTO> getAll() {
        return cartService.getAll().stream().map(cartConverter::toDto).toList();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cartService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

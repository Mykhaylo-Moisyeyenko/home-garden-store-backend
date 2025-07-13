package com.homegarden.store.backend.controller;

import com.homegarden.store.backend.converter.Converter;
import com.homegarden.store.backend.dto.CartItemResponseDTO;
import com.homegarden.store.backend.dto.CreateCartItemRequestDTO;
import com.homegarden.store.backend.dto.UpdateCartItemRequestDTO;
import com.homegarden.store.backend.entity.CartItem;
import com.homegarden.store.backend.service.CartItemService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/cart-items")
public class CartItemController {

    private final CartItemService cartItemService;
    private final Converter<CartItem, CreateCartItemRequestDTO, CartItemResponseDTO> converter;

    @PutMapping("/{id}")
    public ResponseEntity<CartItemResponseDTO> updateQuantity(
            @PathVariable @NotNull @Min(1) Long id,
            @RequestBody @Valid UpdateCartItemRequestDTO dto) {
        CartItem updated = cartItemService.updateQuantity(id, dto.quantity());
        return ResponseEntity.ok(converter.toDto(updated));
    }

    @PostMapping
    public ResponseEntity<CartItemResponseDTO> create(@RequestBody @Valid CreateCartItemRequestDTO dto) {
        CartItem entity = converter.toEntity(dto);
        CartItem created = cartItemService.create(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(converter.toDto(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartItemResponseDTO> getById(@PathVariable @NotNull @Min(1) Long id) {
        return ResponseEntity.ok(converter.toDto(cartItemService.getById(id)));
    }

    @GetMapping
    public List<CartItemResponseDTO> getAll() {
        return cartItemService.getAll().stream()
                .map(converter::toDto)
                .toList();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @NotNull @Min(1) Long id) {
        cartItemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

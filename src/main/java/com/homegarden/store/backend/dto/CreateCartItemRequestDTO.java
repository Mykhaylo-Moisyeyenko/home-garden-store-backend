package com.homegarden.store.backend.dto;

public record CreateCartItemRequestDTO(
        Long cartId,
        Long productId,
        Integer quantity){
}
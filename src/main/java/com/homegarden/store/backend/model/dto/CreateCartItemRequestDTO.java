package com.homegarden.store.backend.model.dto;

public record CreateCartItemRequestDTO(
        Long cartId,
        Long productId,
        Integer quantity){
}

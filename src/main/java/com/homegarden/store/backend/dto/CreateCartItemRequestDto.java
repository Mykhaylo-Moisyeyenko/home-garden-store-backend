package com.homegarden.store.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateCartItemRequestDto(

        @NotNull(message = "Cart ID is required")
        @Positive(message = "Cart ID must be more than 0")
        Long cartId,

        @NotNull(message = "Product ID is required")
        @Positive(message = "Product ID must be more than 0")
        Long productId,

        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be more than 0")
        Integer quantity){
}
package com.homegarden.store.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateCartItemRequestDto(
        @NotNull(message = "Cart Item ID is required")
        @Positive(message = "Cart Item ID must be more than 0")
        Long cartItemId,

        @NotNull
        @Min(0)
        Integer quantity){
}
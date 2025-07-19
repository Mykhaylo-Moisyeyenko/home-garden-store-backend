package com.homegarden.store.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateOrderItemRequestDTO (

    @NotNull(message = "productId name can't be empty")
    @Positive(message = "productId must be more than 0")
    Long productId,

    @NotNull(message = "product quantity can't be empty")
    @Positive(message = "product quantity must be more than 0")
    Integer quantity){
}
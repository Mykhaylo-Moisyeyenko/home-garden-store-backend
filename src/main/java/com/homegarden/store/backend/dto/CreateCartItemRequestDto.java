package com.homegarden.store.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateCartItemRequestDto(
        @NotNull @Min(1) Long productId,
        @NotNull @Min(1) Integer quantity) {

}

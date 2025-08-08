package com.homegarden.store.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateCartItemRequestDto(
        @NotNull
        @Min(1)
        Integer quantity){
}
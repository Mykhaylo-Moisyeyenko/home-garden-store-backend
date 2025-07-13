package com.homegarden.store.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

public record CreateCartRequestDTO(@NotNull @Min(1) Long userId) {
}
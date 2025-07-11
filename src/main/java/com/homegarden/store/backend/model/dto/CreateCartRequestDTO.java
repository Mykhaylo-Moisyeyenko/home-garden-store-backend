package com.homegarden.store.backend.model.dto;

import jakarta.validation.constraints.NotNull;

public record CreateCartRequestDTO(@NotNull Long userId) {
}


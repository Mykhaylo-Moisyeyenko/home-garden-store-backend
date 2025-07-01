package com.homegarden.store.backend.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record FavoriteDto(
        @NotNull
        @Positive
        Long userId,

        @NotNull
        @Positive
        Long productId) {
}

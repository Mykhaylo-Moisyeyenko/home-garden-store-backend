package com.homegarden.store.backend.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record FavoriteDto(

        @NotNull(message = "UserId name can't be empty")
        @Positive(message = "UserId must be more than 0")
        Long userId,

        @NotNull(message = "ProductId name can't be empty")
        @Positive(message = "ProductId must be more than 0")
        Long productId) {
}
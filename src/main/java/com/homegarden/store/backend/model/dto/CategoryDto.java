package com.homegarden.store.backend.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryDto(
        Long categoryId,
        @NotBlank(message = "Название категории не должно быть пустым")
        @Size(max = 100, message = "Название категории не должно превышать 100 символов")
        String name)
{}


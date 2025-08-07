package com.homegarden.store.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryDto(

        Long categoryId,

        @NotBlank(message = "Category name can't be empty")
        @Size(max = 100, message = "Category name can't exceed 100 characters")

        String name){
}
package com.homegarden.store.backend.dto.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequestDto(

        @NotBlank(message = "Email can't be empty")
        @Email(message = "E-mail format is invalid")
        String userEmail,

        @NotBlank(message = "Password can't be empty")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        String password) {
}
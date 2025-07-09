package com.homegarden.store.backend.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequestDTO(

        @NotBlank(message = "Username can't be empty")
        String username,

        @NotBlank(message = "Email can't be empty")
        @Email(message = "E-mail format is invalid")
        String email,

        @NotBlank(message = "Phone number can't be empty")
        String phoneNumber,

        @NotBlank(message = "Password can't be empty")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        String password) {

}
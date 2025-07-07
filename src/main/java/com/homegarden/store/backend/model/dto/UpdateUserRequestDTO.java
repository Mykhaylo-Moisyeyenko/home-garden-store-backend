package com.homegarden.store.backend.model.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequestDTO(

        @NotBlank(message = "Username can't be empty")
        String username,

        @NotBlank(message = "Phone number can't be empty")
        String phoneNumber) {

}


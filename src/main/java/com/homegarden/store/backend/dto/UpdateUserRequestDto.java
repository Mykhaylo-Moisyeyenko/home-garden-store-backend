package com.homegarden.store.backend.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequestDto(

        @NotBlank(message = "Username can't be empty")
        String username,

        @NotBlank(message = "Phone number can't be empty")
        String phoneNumber) {

}
package com.homegarden.store.backend.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequestDTO(

        @NotBlank(message = "Имя пользователя обязательно")
        String username,

        @NotBlank(message = "Email обязателен")
        @Email(message = "Некорректный email")
        String email,

        @NotBlank(message = "Номер телефона обязателен")
        String phoneNumber,

        @NotBlank(message = "Пароль обязателен")
        @Size(min = 8, message = "Пароль должен содержать минимум 8 символов")
        String password) {

}

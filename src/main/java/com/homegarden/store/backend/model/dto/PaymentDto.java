package com.homegarden.store.backend.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

/**
 * DTO для передачи данных о платеже
 */
public record PaymentDto(

        Long id,

        @NotNull(message = "UserId не может быть пустым")
        @Positive(message = "UserId должен быть больше 0")
        Long userId,

        @NotNull(message = "Сумма не может быть пустой")
        @Positive(message = "Сумма должна быть больше 0")
        Double amount,

        @NotNull(message = "Статус не может быть пустым")
        String status, // NEW, PAID, CANCELLED, DELIVERED

        LocalDateTime createdAt
) {}



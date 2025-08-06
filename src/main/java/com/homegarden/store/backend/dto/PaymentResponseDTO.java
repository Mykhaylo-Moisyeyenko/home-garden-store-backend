package com.homegarden.store.backend.dto;

import com.homegarden.store.backend.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponseDTO(

        Long paymentId,

        Long orderId,

        BigDecimal amount,

        PaymentStatus status,

        LocalDateTime createdAt,

        LocalDateTime updatedAt) {
}
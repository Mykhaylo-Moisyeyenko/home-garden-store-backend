package com.homegarden.store.backend.dto;

import com.homegarden.store.backend.enums.PaymentStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record PaymentResponseDto(Long paymentId,
                                 Long orderId,
                                 BigDecimal amount,
                                 PaymentStatus status,
                                 LocalDateTime createdAt,
                                 LocalDateTime updatedAt) {
}
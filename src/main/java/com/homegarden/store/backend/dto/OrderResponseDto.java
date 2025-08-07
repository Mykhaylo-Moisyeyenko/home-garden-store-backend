package com.homegarden.store.backend.dto;

import com.homegarden.store.backend.enums.Status;
import lombok.Builder;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder

public record OrderResponseDto(

        Long orderId,

        Long userId,

        List<OrderItemResponseDto> items,

        LocalDateTime createdAt,

        String deliveryAddress,

        String contactPhone,

        String deliveryMethod,

        Status status,

        LocalDateTime updatedAt,

        BigDecimal totalSum) {
}
package com.homegarden.store.backend.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CreateOrderRequestDTO(
        List<OrderItemDTO> orderItems,
        @NotBlank(message = "Delivery address can't be empty")
        String deliveryAddress,
        @NotBlank(message = "Delivery method can't be empty")
        String deliveryMethod) {
}
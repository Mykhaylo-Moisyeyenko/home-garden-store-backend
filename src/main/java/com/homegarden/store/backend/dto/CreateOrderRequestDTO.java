package com.homegarden.store.backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateOrderRequestDTO(
        @NotEmpty @Valid
        List<CreateOrderItemRequestDTO> orderItems,
        @NotBlank(message = "Delivery address can't be empty")
        String deliveryAddress,
        @NotBlank(message = "Delivery method can't be empty")
        String deliveryMethod) {
}
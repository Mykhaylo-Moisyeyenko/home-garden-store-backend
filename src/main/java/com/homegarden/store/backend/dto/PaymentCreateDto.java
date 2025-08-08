package com.homegarden.store.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PaymentCreateDto(

        @NotNull(message = "Order ID is required")
        @Positive(message = "Order ID must be more than 0")
        Long orderId){        
}
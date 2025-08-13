package com.homegarden.store.backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateOrderRequestDto(@NotNull @NotEmpty @Valid
                                    List<@NotNull CreateOrderItemRequestDto> orderItems,

                                    @NotBlank(message = "Delivery address can't be empty")
                                    String deliveryAddress,

                                    @NotBlank(message = "Delivery method can't be empty")
                                    String deliveryMethod) {
}
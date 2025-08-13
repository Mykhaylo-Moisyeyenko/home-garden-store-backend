package com.homegarden.store.backend.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record OrderItemResponseDto(Long orderId,
                                   Long productId,
                                   Integer quantity,
                                   BigDecimal priceAtPurchase) {

}
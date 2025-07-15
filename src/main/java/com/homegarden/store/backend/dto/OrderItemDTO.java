package com.homegarden.store.backend.dto;

public record OrderItemDTO(Long orderId, Long productId, Integer quantity) {
}
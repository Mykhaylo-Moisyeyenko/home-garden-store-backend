package com.homegarden.store.backend.model.dto;

import java.math.BigDecimal;

public record CartItemResponseDTO(
        Long id,
        Long cartId,
        Long productId,
        String productName,
        Integer quantity,
        BigDecimal price)
{}


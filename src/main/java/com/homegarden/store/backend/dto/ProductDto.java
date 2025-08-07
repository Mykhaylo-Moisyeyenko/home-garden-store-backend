package com.homegarden.store.backend.dto;

import lombok.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Builder

public record ProductDto(

        Long productId,

        String name,

        String description,

        BigDecimal price,

        Long categoryId,

        String imageUrl,

        BigDecimal discountPrice,

        Timestamp createdAt,

        Timestamp updatedAt) {
}
package com.homegarden.store.backend.dto;

public record TopCancelledProductDto(

        Long productId,

        String productName,

        Long cancelCount) {
}
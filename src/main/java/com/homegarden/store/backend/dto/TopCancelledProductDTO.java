package com.homegarden.store.backend.dto;

public record TopCancelledProductDTO(

        Long productId,

        String productName,

        Long cancelCount) {
}
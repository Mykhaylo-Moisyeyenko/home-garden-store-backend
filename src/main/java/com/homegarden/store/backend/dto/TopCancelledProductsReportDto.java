package com.homegarden.store.backend.dto;

public record TopCancelledProductsReportDto(

        Long productId,

        String productName,

        Long cancelCount) {
}
package com.homegarden.store.backend.dto;

import java.math.BigDecimal;

public record TopTenSelledProductsReportDto(Long productId,
                                            Long totalQuantity,
                                            BigDecimal totalSum) {
}
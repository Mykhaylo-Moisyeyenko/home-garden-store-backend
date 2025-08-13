package com.homegarden.store.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProfitReportDto(LocalDateTime startDateTime,
                              BigDecimal profit) {
}
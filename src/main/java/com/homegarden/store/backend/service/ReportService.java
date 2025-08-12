package com.homegarden.store.backend.service;

import com.homegarden.store.backend.dto.ProfitReportDto;
import com.homegarden.store.backend.dto.TopCancelledProductDto;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {

    List<TopCancelledProductDto> getTopCancelledProducts();

    List<ProfitReportDto> getProfitReport(LocalDate startDate, LocalDate endDate, String groupBy);
}
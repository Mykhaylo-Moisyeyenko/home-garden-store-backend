package com.homegarden.store.backend.service;

import com.homegarden.store.backend.dto.ProfitReportDto;
import com.homegarden.store.backend.dto.TopCancelledProductsReportDto;
import com.homegarden.store.backend.dto.TopTenSelledProductsReportDto;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {

    List<TopCancelledProductsReportDto> getTopCancelledProducts();

    List<ProfitReportDto> getProfitReport(LocalDate startDate, LocalDate endDate, String groupBy);

    List<TopTenSelledProductsReportDto> getTopTenSelledProducts(String sortBy);
}
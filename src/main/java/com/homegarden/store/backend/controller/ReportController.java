package com.homegarden.store.backend.controller;

import com.homegarden.store.backend.dto.ProfitReportDto;
import com.homegarden.store.backend.dto.TopCancelledProductDto;
import com.homegarden.store.backend.exception.ReportBadRequestException;
import com.homegarden.store.backend.service.ReportService;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/v1/reports")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasRole('ADMINISTRATOR')")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/top-cancelled-products")
    public ResponseEntity<List<TopCancelledProductDto>> getTopCancelledProducts() {
        reportService.getTopCancelledProducts();

        return ResponseEntity.ok().body(reportService.getTopCancelledProducts());
    }

    @GetMapping("/profit-report")
    public ResponseEntity<List<ProfitReportDto>> getProfitReport(
            @RequestParam
            @PastOrPresent(message = "Start date can't be in the future")
            LocalDate startDate,

            @RequestParam
            @PastOrPresent(message = "End date can't be in the future")
            LocalDate endDate,

            @RequestParam @Pattern(regexp = "hour|day|week|month")
            String groupBy) {

        if(startDate.isAfter(endDate)) {
            throw new ReportBadRequestException("Start date can't be in the future");
        }

        return ResponseEntity.ok(reportService.getProfitReport(startDate, endDate, groupBy));
    }
}
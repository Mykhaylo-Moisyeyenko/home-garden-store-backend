package com.homegarden.store.backend.controller;

import com.homegarden.store.backend.converter.OrderConverter;
import com.homegarden.store.backend.dto.OrderResponseDto;
import com.homegarden.store.backend.dto.ProfitReportDto;
import com.homegarden.store.backend.dto.TopCancelledProductsReportDto;
import com.homegarden.store.backend.dto.TopTenSelledProductsReportDto;
import com.homegarden.store.backend.exception.ReportBadRequestException;
import com.homegarden.store.backend.service.ReportService;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import static java.util.Arrays.stream;

@RestController
@RequestMapping("/v1/reports")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasRole('ADMINISTRATOR')")
public class ReportController {

    private final ReportService reportService;
    private final OrderConverter converter;

    @GetMapping("/top-cancelled-products")
    public ResponseEntity<List<TopCancelledProductsReportDto>> getTopCancelledProducts() {
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

    @GetMapping("/top-ten-selled-products")
    public ResponseEntity<List<TopTenSelledProductsReportDto>> getTopTenSelledProducts(
            @RequestParam @Pattern(regexp = "quantity|sum") String sortBy) {

        return ResponseEntity.ok(reportService.getTopTenSelledProducts(sortBy));
    }

    @GetMapping("/orders-awaiting-payment")
    public ResponseEntity<List<OrderResponseDto>> getOrdersAwaitingPayment(
            @RequestParam @Positive int days){
        return ResponseEntity.ok(reportService.getAwaitingPaymentOrders(days).stream()
                .map(converter::toDto)
                .toList());
    }
}
package com.homegarden.store.backend.controller;

import com.homegarden.store.backend.controller.api.ReportControllerApi;
import com.homegarden.store.backend.converter.OrderConverter;
import com.homegarden.store.backend.dto.OrderResponseDto;
import com.homegarden.store.backend.dto.ProfitReportDto;
import com.homegarden.store.backend.dto.TopCancelledProductsReportDto;
import com.homegarden.store.backend.dto.TopTenSelledProductsReportDto;
import com.homegarden.store.backend.entity.Order;
import com.homegarden.store.backend.service.ReportService;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor

public class ReportController implements ReportControllerApi {

    private final ReportService reportService;
    private final OrderConverter orderConverter;

    @Override
    public ResponseEntity<List<TopCancelledProductsReportDto>> getTopCancelledProducts() {

        return ResponseEntity.ok(reportService.getTopCancelledProducts());
    }

    @Override
    public ResponseEntity<List<ProfitReportDto>> getProfitReport(
            @PastOrPresent @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @PastOrPresent @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            String groupBy) {

        return ResponseEntity.ok(reportService.getProfitReport(startDate, endDate, groupBy));
    }

    @Override
    public ResponseEntity<List<TopTenSelledProductsReportDto>> getTopTenSelledProducts(String sortBy) {

        return ResponseEntity.ok(reportService.getTopTenSelledProducts(sortBy));
    }

    @Override
    public ResponseEntity<List<OrderResponseDto>> getOrdersAwaitingPayment(@Positive int days) {
        List<Order> orders = reportService.getAwaitingPaymentOrders(days);
        List<OrderResponseDto> dtos = orders.stream()
                .map(orderConverter::toDto)
                .toList();

        return ResponseEntity.ok(dtos);
    }
}

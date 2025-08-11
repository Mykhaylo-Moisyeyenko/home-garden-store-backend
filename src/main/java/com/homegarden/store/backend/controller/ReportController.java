package com.homegarden.store.backend.controller;

import com.homegarden.store.backend.dto.TopCancelledProductDto;
import com.homegarden.store.backend.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/reports")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMINISTRATOR')")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/top-cancelled-products")
    public ResponseEntity<List<TopCancelledProductDto>> getTopCancelledProducts() {
        reportService.getTopCancelledProducts();

        return ResponseEntity.ok().body(reportService.getTopCancelledProducts());
    }

}
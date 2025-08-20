package com.homegarden.store.backend.controller;

import com.homegarden.store.backend.controller.api.OrderControllerApi;
import com.homegarden.store.backend.converter.OrderConverter;
import com.homegarden.store.backend.dto.CreateOrderRequestDto;
import com.homegarden.store.backend.dto.OrderResponseDto;
import com.homegarden.store.backend.entity.Order;
import com.homegarden.store.backend.enums.Status;
import com.homegarden.store.backend.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
@Validated
public class OrderController implements OrderControllerApi {

    private final OrderService orderService;
    private final OrderConverter converter;

    @Override
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<OrderResponseDto> create(@RequestBody @Valid CreateOrderRequestDto request) {
        Order created = orderService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(converter.toDto(created));
    }

    @Override
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAll() {
        List<OrderResponseDto> result = orderService.getAll()
                .stream().map(converter::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/history")
    public ResponseEntity<List<OrderResponseDto>> getAllByUser() {
        List<OrderResponseDto> result = orderService.getAllByUser()
                .stream().map(converter::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getById(@PathVariable("id") @Min(1) Long id) {
        Order order = orderService.getById(id);
        return ResponseEntity.ok(converter.toDto(order));
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable("id") @Min(1) Long id) {
        orderService.cancel(id);
        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping("/revenue")
    public ResponseEntity<List<Map<String, Object>>> getGroupedRevenue(
            @RequestParam(name = "startPeriod") String startPeriod,
            @RequestParam(name = "endPeriod") String endPeriod,
            @RequestParam(name = "timeCut") @Pattern(regexp = "hour|day|week|month") String timeCut) {

        LocalDateTime start = LocalDateTime.parse(startPeriod);
        LocalDateTime end = LocalDateTime.parse(endPeriod);

        List<Map<String, Object>> series = orderService.getGroupedRevenue(start, end, timeCut)
                .stream()
                .map(row -> Map.of(
                        "period", row[0],
                        "revenue", row[1]
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(series);
    }

    @Override
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping("/status/before")
    public ResponseEntity<List<OrderResponseDto>> getAllByStatusAndUpdatedAtBefore(
            @RequestParam Status status,
            @RequestParam String updatedAtBefore) {

        LocalDateTime before = LocalDateTime.parse(updatedAtBefore);
        List<OrderResponseDto> result = orderService
                .getAllByStatusAndUpdatedAtBefore(status, before)
                .stream().map(converter::toDto).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @Override
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping("/status/after")
    public ResponseEntity<List<OrderResponseDto>> getAllByStatusAndUpdatedAtAfter(
            @RequestParam Status status,
            @RequestParam String updatedAtAfter) {

        LocalDateTime after = LocalDateTime.parse(updatedAtAfter);
        List<OrderResponseDto> result = orderService
                .getAllByStatusAndUpdatedAtAfter(status, after)
                .stream().map(converter::toDto).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }
}

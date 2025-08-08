package com.homegarden.store.backend.controller;

import com.homegarden.store.backend.converter.OrderConverter;
import com.homegarden.store.backend.dto.CreateOrderRequestDto;
import com.homegarden.store.backend.dto.OrderResponseDto;
import com.homegarden.store.backend.dto.TopCancelledProductDto;
import com.homegarden.store.backend.entity.Order;
import com.homegarden.store.backend.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/orders")

public class OrderController {

    private final OrderService orderService;
    private final OrderConverter converter;

    @PostMapping
    public ResponseEntity<OrderResponseDto> create(@RequestBody @NotNull @Valid CreateOrderRequestDto orderRequestDTO) {

        Order order = orderService.create(orderRequestDTO);
        OrderResponseDto response = converter.toDto(order);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAll() {
        List<OrderResponseDto> response = orderService

                .getAll().stream()
                .map(converter::toDto)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getById(@PathVariable @Valid @Min(1) Long orderId) {
        Order order = orderService.getById(orderId);
        OrderResponseDto response = converter.toDto(order);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<List<OrderResponseDto>> getAllByUserId(@PathVariable @Valid @Min(1) Long userId) {
        List<OrderResponseDto> result = orderService

                .getAllByUserId(userId)
                .stream()
                .map(converter::toDto)
                .toList();

        return ResponseEntity.ok(result);
    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable @Valid @Min(1) Long orderId) {
        orderService.cancel(orderId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/report/top-cancelled-products")
    public ResponseEntity<List<TopCancelledProductDto>> getTopCancelledProducts() {
        orderService.getTopCancelledProducts();

        return ResponseEntity.ok().body(orderService.getTopCancelledProducts());
    }
}
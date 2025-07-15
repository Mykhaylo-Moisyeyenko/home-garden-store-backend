package com.homegarden.store.backend.controller;

import com.homegarden.store.backend.converter.Converter;
import com.homegarden.store.backend.dto.CreateOrderRequestDTO;
import com.homegarden.store.backend.dto.OrderResponseDTO;
import com.homegarden.store.backend.entity.Order;
import com.homegarden.store.backend.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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

    private final Converter<Order, CreateOrderRequestDTO, OrderResponseDTO> converter;

    @PostMapping
    public ResponseEntity<OrderResponseDTO> create(@RequestBody @Valid CreateOrderRequestDTO orderRequestDTO) {
        Order entity = converter.toEntity(orderRequestDTO);
        Order order = orderService.create(entity);
        OrderResponseDTO response = converter.toDto(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAll() {
        List<OrderResponseDTO> response = orderService.getAll().stream()
                .map(converter::toDto)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderResponseDTO> getById(@PathVariable @Valid @Min(1) Long id) {
        Order order = orderService.getById(id);
        OrderResponseDTO response = converter.toDto(order);
        return ResponseEntity.ok(response);
    }
}
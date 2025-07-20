package com.homegarden.store.backend.controller;

import com.homegarden.store.backend.converter.Converter;
import com.homegarden.store.backend.dto.CreateOrderRequestDTO;
import com.homegarden.store.backend.dto.OrderResponseDTO;
import com.homegarden.store.backend.dto.TopCancelledProductDTO;
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
    private final Converter<Order, CreateOrderRequestDTO, OrderResponseDTO> converter;

    @PostMapping
    public ResponseEntity<OrderResponseDTO> create(@RequestBody @NotNull @Valid CreateOrderRequestDTO orderRequestDTO) {
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

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> getById(@PathVariable @Valid @Min(1) Long orderId) {
        Order order = orderService.getById(orderId);
        OrderResponseDTO response = converter.toDto(order);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrdersByUserId(@PathVariable @Valid @Min(1) Long userId) {
        List<OrderResponseDTO> result = orderService.getAllOrdersByUserId(userId).stream()
                .map(converter::toDto)
                .toList();
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable @Valid @Min(1) Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/report/top-cancelled-products")
    public ResponseEntity<List<TopCancelledProductDTO>> getTopCancelledProducts(){
        orderService.getTopCancelledProducts();
        return ResponseEntity.ok().body(orderService.getTopCancelledProducts());
    }
}
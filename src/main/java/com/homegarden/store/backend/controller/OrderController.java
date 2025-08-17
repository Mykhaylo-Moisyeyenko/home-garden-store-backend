package com.homegarden.store.backend.controller;

import com.homegarden.store.backend.controller.api.OrderControllerApi;
import com.homegarden.store.backend.converter.OrderConverter;
import com.homegarden.store.backend.dto.CreateOrderRequestDto;
import com.homegarden.store.backend.dto.OrderResponseDto;
import com.homegarden.store.backend.entity.Order;
import com.homegarden.store.backend.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderController implements OrderControllerApi {

    private final OrderService orderService;
    private final OrderConverter converter;

    @Override
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<OrderResponseDto> create(@Valid @NotNull CreateOrderRequestDto orderRequestDTO) {
        Order order = orderService.create(orderRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(converter.toDto(order));
    }

    @Override
    public ResponseEntity<List<OrderResponseDto>> getAll() {
        List<Order> orders = orderService.getAll();
        List<OrderResponseDto> result = orders.stream().map(converter::toDto).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @Override
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<OrderResponseDto> getById(@Min(1) Long orderId) {
        Order order = orderService.getById(orderId);

        return ResponseEntity.ok(converter.toDto(order));
    }

    @Override
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<List<OrderResponseDto>> getAllByUser() {
        List<Order> orders = orderService.getAllByUser();
        List<OrderResponseDto> result = orders.stream().map(converter::toDto).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @Override
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<Void> cancel(@Min(1) Long orderId) {
        orderService.cancel(orderId);

        return ResponseEntity.ok().build();
    }
}

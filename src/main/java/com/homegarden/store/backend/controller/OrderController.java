package com.homegarden.store.backend.controller;

import com.homegarden.store.backend.controller.api.OrderControllerApi;
import com.homegarden.store.backend.converter.Converter;
import com.homegarden.store.backend.dto.CreateOrderRequestDto;
import com.homegarden.store.backend.dto.OrderResponseDto;
import com.homegarden.store.backend.entity.Order;
import com.homegarden.store.backend.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/orders")
@PreAuthorize("hasRole('ADMINISTRATOR')")
public class OrderController implements OrderControllerApi {

    private final OrderService orderService;
    private final Converter<Order, CreateOrderRequestDto, OrderResponseDto> converter;

    @Override
    @PreAuthorize("hasAnyRole('USER', 'ADMINISTRATOR')")
    public ResponseEntity<OrderResponseDto> create(@Valid @RequestBody CreateOrderRequestDto orderRequestDTO) {
        Order order = orderService.create(orderRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(converter.toDto(order));
    }

    @Override
    public ResponseEntity<List<OrderResponseDto>> getAll() {
        List<OrderResponseDto> orders = orderService.getAll()
                .stream()
                .map(converter::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(orders);
    }

    @Override
    @PreAuthorize("hasAnyRole('USER', 'ADMINISTRATOR')")
    public ResponseEntity<OrderResponseDto> getById(@PathVariable Long orderId) {
        Order order = orderService.getById(orderId);

        return ResponseEntity.ok(converter.toDto(order));
    }

    @Override
    @PreAuthorize("hasAnyRole('USER', 'ADMINISTRATOR')")
    public ResponseEntity<List<OrderResponseDto>> getAllByUser() {
        List<OrderResponseDto> orders = orderService.getAllByUser()
                .stream()
                .map(converter::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(orders);
    }

    @Override
    @PreAuthorize("hasAnyRole('USER', 'ADMINISTRATOR')")
    public ResponseEntity<Void> cancel(@PathVariable Long orderId) {
        orderService.cancel(orderId);

        return ResponseEntity.ok().build();
    }
}

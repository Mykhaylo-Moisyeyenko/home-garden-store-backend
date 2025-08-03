package com.homegarden.store.backend.controller;

import com.homegarden.store.backend.converter.Converter;
import com.homegarden.store.backend.dto.PaymentCreateDTO;
import com.homegarden.store.backend.dto.PaymentResponseDTO;
import com.homegarden.store.backend.entity.Order;
import com.homegarden.store.backend.entity.Payment;
import com.homegarden.store.backend.enums.PaymentStatus;
import com.homegarden.store.backend.service.OrderService;
import com.homegarden.store.backend.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final Converter<Payment, PaymentCreateDTO, PaymentResponseDTO> converter;
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<PaymentResponseDTO>> getAllPayments() {
        List<PaymentResponseDTO> result = paymentService.getAllPayments().stream()
                .map(converter::toDto)
                .toList();
        return ResponseEntity.ok().body(result);
    }

    @PostMapping
    public ResponseEntity<PaymentResponseDTO> create(@RequestBody @Valid PaymentCreateDTO dto) {
        Payment payment = converter.toEntity(dto);
        Payment savedPayment = paymentService.create(payment);
        PaymentResponseDTO paymentResponseDTO = converter.toDto(savedPayment);
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentResponseDTO);
    }

    @PostMapping("/{paymentId}/confirmation")
    public ResponseEntity<PaymentResponseDTO> confirm(
            @PathVariable Long paymentId,
            @RequestParam(defaultValue = "SUCCESS") PaymentStatus status) {
        Payment payment = paymentService.confirm(paymentId, status);
        PaymentResponseDTO paymentResponseDTO = converter.toDto(payment);
        return ResponseEntity.status(HttpStatus.OK).body(paymentResponseDTO);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponseDTO> getById(@PathVariable Long paymentId) {
        Payment payment = paymentService.getById(paymentId);
        return ResponseEntity.status(HttpStatus.OK).body(converter.toDto(payment));
    }

    @GetMapping("/payments-by-order/{orderId}")
    public ResponseEntity<List<PaymentResponseDTO>> getPaymentsByOrder(@PathVariable Long orderId) {
        Order order = orderService.getById(orderId);
        List<PaymentResponseDTO> paymentList = paymentService.getPaymentsByOrder(order).stream()
                .map(converter::toDto)
                .toList();
        return ResponseEntity.ok().body(paymentList);
    }
}
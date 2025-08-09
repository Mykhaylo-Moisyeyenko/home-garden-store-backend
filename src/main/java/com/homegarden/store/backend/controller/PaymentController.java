package com.homegarden.store.backend.controller;

import com.homegarden.store.backend.converter.Converter;
import com.homegarden.store.backend.dto.PaymentCreateDto;
import com.homegarden.store.backend.dto.PaymentResponseDto;
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
    private final Converter<Payment, PaymentCreateDto, PaymentResponseDto> converter;

    @GetMapping
    public ResponseEntity<List<PaymentResponseDto>> getAllPayments() {
        List<PaymentResponseDto> result = paymentService
                .getAllPayments()
                .stream()
                .map(converter::toDto)
                .toList();

        return ResponseEntity.ok().body(result);
    }

    @PostMapping
    public ResponseEntity<PaymentResponseDto> create(@RequestBody @Valid PaymentCreateDto dto) {
        Payment savedPayment = paymentService.create(converter.toEntity(dto));
        PaymentResponseDto paymentResponseDTO = converter.toDto(savedPayment);

        return ResponseEntity.status(HttpStatus.CREATED).body(paymentResponseDTO);
    }

    @PostMapping("/{paymentId}/confirmation")
    public ResponseEntity<PaymentResponseDto> confirm(
            @PathVariable Long paymentId,
            @RequestParam(defaultValue = "SUCCESS") PaymentStatus status) {

        Payment payment = paymentService.confirm(paymentId, status);
        PaymentResponseDto paymentResponseDTO = converter.toDto(payment);

        return ResponseEntity.status(HttpStatus.OK).body(paymentResponseDTO);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponseDto> getById(@PathVariable Long paymentId) {

        Payment payment = paymentService.getById(paymentId);

        return ResponseEntity.status(HttpStatus.OK).body(converter.toDto(payment));
    }

    @GetMapping("/payments-by-order/{orderId}")
    public ResponseEntity<List<PaymentResponseDto>> getPaymentsByOrder(@PathVariable Long orderId) {
        List<PaymentResponseDto> paymentList = paymentService
                .getPaymentsByOrder(orderId)
                .stream()
                .map(converter::toDto)
                .toList();

        return ResponseEntity.ok().body(paymentList);
    }
}
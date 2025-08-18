package com.homegarden.store.backend.controller;

import com.homegarden.store.backend.controller.api.PaymentControllerApi;
import com.homegarden.store.backend.converter.Converter;
import com.homegarden.store.backend.dto.PaymentCreateDto;
import com.homegarden.store.backend.dto.PaymentResponseDto;
import com.homegarden.store.backend.entity.Payment;
import com.homegarden.store.backend.enums.PaymentStatus;
import com.homegarden.store.backend.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PaymentController implements PaymentControllerApi {

    private final PaymentService paymentService;
    private final Converter<Payment, PaymentCreateDto, PaymentResponseDto> converter;

    @Override
    public ResponseEntity<List<PaymentResponseDto>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        List<PaymentResponseDto> result = payments.stream()
                .map(converter::toDto)
                .toList();

        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<PaymentResponseDto> create(@Valid PaymentCreateDto dto) {
        Payment entity = converter.toEntity(dto);
        Payment saved = paymentService.create(entity);
        PaymentResponseDto response = converter.toDto(saved);

        return ResponseEntity.status(201).body(response);
    }

    @Override
    public ResponseEntity<PaymentResponseDto> confirm(Long paymentId, PaymentStatus status) {
        Payment updated = paymentService.confirm(paymentId, status);

        return ResponseEntity.ok(converter.toDto(updated));
    }

    @Override
    public ResponseEntity<PaymentResponseDto> getById(Long paymentId) {
        Payment payment = paymentService.getById(paymentId);

        return ResponseEntity.ok(converter.toDto(payment));
    }

    @Override
    public ResponseEntity<List<PaymentResponseDto>> getPaymentsByOrder(Long orderId) {
        List<Payment> payments = paymentService.getAllByOrder(orderId);
        List<PaymentResponseDto> result = payments.stream()
                .map(converter::toDto)
                .toList();

        return ResponseEntity.ok(result);
    }
}

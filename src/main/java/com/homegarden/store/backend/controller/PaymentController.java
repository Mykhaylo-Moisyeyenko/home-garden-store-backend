package com.homegarden.store.backend.controller;

import com.homegarden.store.backend.controller.api.PaymentControllerApi;
import com.homegarden.store.backend.converter.Converter;
import com.homegarden.store.backend.dto.PaymentCreateDto;
import com.homegarden.store.backend.dto.PaymentResponseDto;
import com.homegarden.store.backend.entity.Payment;
import com.homegarden.store.backend.enums.PaymentStatus;
import com.homegarden.store.backend.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated 
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/payments")
public class PaymentController implements PaymentControllerApi {

    private final PaymentService paymentService;
    private final Converter<Payment, PaymentCreateDto, PaymentResponseDto> converter;

    @Override
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping
    public ResponseEntity<List<PaymentResponseDto>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        List<PaymentResponseDto> result = payments.stream()
                .map(converter::toDto)
                .toList();

        return ResponseEntity.ok(result);
    }

    @Override   
    @PreAuthorize("hasAnyRole('USER','ADMINISTRATOR')")
    @PostMapping
    public ResponseEntity<PaymentResponseDto> create(@RequestBody @Valid PaymentCreateDto dto) {
        Payment entity = converter.toEntity(dto);
        Payment saved = paymentService.create(entity);
        PaymentResponseDto response = converter.toDto(saved);

        return ResponseEntity.status(201).body(response);
    }

    @Override
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PostMapping("/{paymentId}/confirmation")
    public ResponseEntity<PaymentResponseDto> confirm(@PathVariable("paymentId") @Min(1) Long paymentId) {
                                                      @RequestParam(defaultValue = "SUCCESS") PaymentStatus status) {
        Payment updated = paymentService.confirm(paymentId, status);
        return ResponseEntity.ok(converter.toDto(updated));
    }

    @Override
    @PreAuthorize("hasAnyRole('USER','ADMINISTRATOR')")
    @GetMapping("/{paymentId}")    
    public ResponseEntity<PaymentResponseDto> getById(@PathVariable("paymentId") @Min(1) Long paymentId) {
        Payment payment = paymentService.getById(paymentId);
        return ResponseEntity.ok(converter.toDto(payment));
    }

    @Override
    @PreAuthorize("hasAnyRole('USER','ADMINISTRATOR')")
    @GetMapping("/payments-by-order/{orderId}")
    public ResponseEntity<List<PaymentResponseDto>> getPaymentsByOrder(@PathVariable("orderId") @Min(1) Long orderId) {
        List<Payment> payments = paymentService.getAllByOrder(orderId);
        List<PaymentResponseDto> result = payments.stream()
                .map(converter::toDto)
                .toList();

        return ResponseEntity.ok(result);
    }
}

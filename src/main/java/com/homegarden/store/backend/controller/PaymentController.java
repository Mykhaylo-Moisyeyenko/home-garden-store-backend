package com.homegarden.store.backend.controller;

import com.homegarden.store.backend.converter.PaymentConverter;
import com.homegarden.store.backend.model.dto.PaymentDto;
import com.homegarden.store.backend.model.entity.Payment;
import com.homegarden.store.backend.service.PaymentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
@Validated
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentConverter paymentConverter;

    /** Создать новый платёж */
    @PostMapping
    public ResponseEntity<PaymentDto> create(@RequestBody @Valid PaymentDto dto) {
        Payment payment = paymentConverter.toEntity(dto);
        Payment created = paymentService.create(payment);
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentConverter.toDto(created));
    }

    /** Получить все платежи */
    @GetMapping
    public ResponseEntity<List<PaymentDto>> getAll() {
        List<PaymentDto> payments = paymentService.getAll().stream()
                .map(paymentConverter::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(payments);
    }

    /** Получить платёж по ID */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentDto> getById(@PathVariable @Min(1) Long id) {
        Payment payment = paymentService.getById(id);
        return ResponseEntity.ok(paymentConverter.toDto(payment));
    }

    /** Обновить статус платежа */
    @PutMapping("/{id}/status")
    public ResponseEntity<PaymentDto> updateStatus(
            @PathVariable @Min(1) Long id,
            @RequestParam("status") String status
    ) {
        Payment updated = paymentService.updateStatus(id, status);
        return ResponseEntity.ok(paymentConverter.toDto(updated));
    }

    /** Удалить платёж */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Min(1) Long id) {
        paymentService.delete(id);
    }
}



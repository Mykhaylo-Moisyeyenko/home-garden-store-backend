package com.homegarden.store.backend.converter;

import com.homegarden.store.backend.dto.PaymentCreateDto;
import com.homegarden.store.backend.dto.PaymentResponseDto;
import com.homegarden.store.backend.entity.Order;
import com.homegarden.store.backend.entity.Payment;
import org.springframework.stereotype.Component;

@Component

public class PaymentConverter implements Converter<Payment, PaymentCreateDto, PaymentResponseDto> {

    public Payment toEntity(PaymentCreateDto dto) {

        return Payment
                .builder()
                .order(Order.builder().orderId(dto.orderId()).build())
                .build();
    }

    public PaymentResponseDto toDto(Payment payment) {

        return new PaymentResponseDto(
                payment.getId(),
                payment.getOrder().getOrderId(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getCreatedAt(),
                payment.getUpdatedAt());
    }
}
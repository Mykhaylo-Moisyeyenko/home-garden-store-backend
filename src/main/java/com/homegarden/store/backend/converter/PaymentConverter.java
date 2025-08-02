package com.homegarden.store.backend.converter;

import com.homegarden.store.backend.dto.PaymentCreateDTO;
import com.homegarden.store.backend.dto.PaymentResponseDTO;
import com.homegarden.store.backend.entity.Order;
import com.homegarden.store.backend.entity.Payment;

public class PaymentConverter {

    public static Payment toEntity(PaymentCreateDTO dto, Order order) {
        return Payment.builder()
                .order(order)
                .amount(dto.getAmount())
                .status(dto.getStatus())
                .build();
    }

    public static PaymentResponseDTO toResponseDTO(Payment payment) {
        return PaymentResponseDTO.builder()
                .paymentId(payment.getId())
                .orderId(payment.getOrder().getOrderId())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }
}


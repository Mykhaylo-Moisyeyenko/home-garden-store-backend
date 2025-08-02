package com.homegarden.store.backend.converter;

import com.homegarden.store.backend.dto.PaymentResponseDTO;
import com.homegarden.store.backend.entity.Order;
import com.homegarden.store.backend.entity.Payment;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PaymentConverter {

    public Payment toEntity(Long orderId, BigDecimal amount) {
        return Payment.builder()
                .order(Order.builder().orderId(orderId).build())
                .amount(amount)
                .build();
    }

    public PaymentResponseDTO toResponseDTO(Payment payment) {
        return new PaymentResponseDTO(
                payment.getPaymentId(),
                payment.getOrder().getOrderId(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getCreatedAt(),
                payment.getUpdatedAt()
        );
    }
}


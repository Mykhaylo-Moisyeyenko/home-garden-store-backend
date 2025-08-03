package com.homegarden.store.backend.converter;

import com.homegarden.store.backend.dto.PaymentCreateDTO;
import com.homegarden.store.backend.dto.PaymentResponseDTO;
import com.homegarden.store.backend.entity.Order;
import com.homegarden.store.backend.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentConverter {

    public Payment toEntity(PaymentCreateDTO dto) {
        return Payment.builder()
                .order(Order.builder().orderId(dto.orderId()).build())
                .amount(dto.amount())
                .build();
    }

    public PaymentResponseDTO toResponseDTO(Payment payment) {
        return new PaymentResponseDTO(
                payment.getId(),
                payment.getOrder().getOrderId(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getCreatedAt(),
                payment.getUpdatedAt()
        );
    }
}


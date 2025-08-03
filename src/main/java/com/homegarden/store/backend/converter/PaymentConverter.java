package com.homegarden.store.backend.converter;

import com.homegarden.store.backend.dto.PaymentCreateDTO;
import com.homegarden.store.backend.dto.PaymentResponseDTO;
import com.homegarden.store.backend.entity.Order;
import com.homegarden.store.backend.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentConverter implements Converter<Payment, PaymentCreateDTO, PaymentResponseDTO> {

    public Payment toEntity(PaymentCreateDTO dto) {
        return Payment.builder()
                .order(Order.builder().orderId(dto.orderId()).build())
                .build();
    }

    public PaymentResponseDTO toDto(Payment payment) {
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
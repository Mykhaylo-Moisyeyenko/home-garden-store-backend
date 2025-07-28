package com.homegarden.store.backend.converter;

import com.homegarden.store.backend.model.dto.PaymentDto;
import com.homegarden.store.backend.model.entity.Payment;
import com.homegarden.store.backend.model.entity.PaymentStatus;
import org.springframework.stereotype.Component;

@Component
public class PaymentConverter {

    public PaymentDto toDto(Payment payment) {
        return new PaymentDto(
                payment.getId(),
                payment.getUserId(),
                payment.getAmount(),
                payment.getStatus().name(),
                payment.getCreatedAt()
        );
    }

    public Payment toEntity(PaymentDto dto) {
        Payment payment = new Payment();
        payment.setId(dto.id());
        payment.setUserId(dto.userId());
        payment.setAmount(dto.amount());

        // Преобразуем строку в Enum
        if (dto.status() != null) {
            payment.setStatus(PaymentStatus.valueOf(dto.status().toUpperCase()));
        } else {
            payment.setStatus(PaymentStatus.NEW);
        }

        // Если в DTO нет createdAt — ставим текущую дату
        payment.setCreatedAt(dto.createdAt() != null ? dto.createdAt() : java.time.LocalDateTime.now());

        return payment;
    }
}


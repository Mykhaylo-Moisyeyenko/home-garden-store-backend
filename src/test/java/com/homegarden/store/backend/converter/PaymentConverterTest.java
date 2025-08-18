package com.homegarden.store.backend.converter;

import com.homegarden.store.backend.dto.PaymentCreateDto;
import com.homegarden.store.backend.dto.PaymentResponseDto;
import com.homegarden.store.backend.entity.Order;
import com.homegarden.store.backend.entity.Payment;
import com.homegarden.store.backend.enums.PaymentStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentConverterTest {

    private final PaymentConverter converter = new PaymentConverter();

    @Test
    void toEntity_shouldMapFieldsCorrectly() {
        Long orderId = 42L;
        PaymentCreateDto dto = new PaymentCreateDto(orderId);

        Payment entity = converter.toEntity(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getOrder()).isNotNull();
        assertThat(entity.getOrder().getOrderId()).isEqualTo(orderId);
    }

    @Test
    void toDto_shouldMapFieldsCorrectly() {
        Long paymentId = 1L;
        Long orderId = 100L;
        BigDecimal amount = BigDecimal.valueOf(200);
        PaymentStatus status = PaymentStatus.SUCCESS;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        Order order = Order.builder()
                .orderId(orderId)
                .build();

        Payment payment = Payment.builder()
                .id(paymentId)
                .order(order)
                .amount(amount)
                .status(status)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        PaymentResponseDto dto = converter.toDto(payment);

        assertThat(dto).isNotNull();
        assertThat(dto.paymentId()).isEqualTo(paymentId);
        assertThat(dto.orderId()).isEqualTo(orderId);
        assertThat(dto.amount()).isEqualTo(amount);
        assertThat(dto.status()).isEqualTo(status);
        assertThat(dto.createdAt()).isEqualTo(createdAt);
        assertThat(dto.updatedAt()).isEqualTo(updatedAt);
    }
}

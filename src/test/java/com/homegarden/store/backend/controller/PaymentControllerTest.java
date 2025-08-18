package com.homegarden.store.backend.controller;

import com.homegarden.store.backend.converter.PaymentConverter;
import com.homegarden.store.backend.dto.PaymentCreateDto;
import com.homegarden.store.backend.dto.PaymentResponseDto;
import com.homegarden.store.backend.entity.Payment;
import com.homegarden.store.backend.enums.PaymentStatus;
import com.homegarden.store.backend.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @Mock
    private PaymentConverter paymentConverter;

    @InjectMocks
    private PaymentController paymentController;

    private Payment payment;
    private PaymentResponseDto responseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        payment = Payment.builder()
                .id(1L)
                .amount(BigDecimal.valueOf(100))
                .status(PaymentStatus.SUCCESS)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        responseDto = new PaymentResponseDto(
                1L, 2L, BigDecimal.valueOf(100), PaymentStatus.SUCCESS,
                payment.getCreatedAt(), payment.getUpdatedAt()
        );
    }

    @Test
    void getAllPayments_shouldReturnList() {
        when(paymentService.getAllPayments()).thenReturn(List.of(payment));
        when(paymentConverter.toDto(any())).thenReturn(responseDto);

        ResponseEntity<List<PaymentResponseDto>> response = paymentController.getAllPayments();
        List<PaymentResponseDto> result = response.getBody();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).paymentId()).isEqualTo(1L);
    }

    @Test
    void create_shouldReturnCreatedPayment() {
        PaymentCreateDto dto = new PaymentCreateDto(2L);

        when(paymentConverter.toEntity(any())).thenReturn(payment);
        when(paymentService.create(any())).thenReturn(payment);
        when(paymentConverter.toDto(any())).thenReturn(responseDto);

        ResponseEntity<PaymentResponseDto> response = paymentController.create(dto);
        PaymentResponseDto result = response.getBody();

        assertThat(result.paymentId()).isEqualTo(1L);
    }

    @Test
    void confirm_shouldReturnUpdatedPayment() {
        when(paymentService.confirm(eq(1L), eq(PaymentStatus.SUCCESS))).thenReturn(payment);
        when(paymentConverter.toDto(any())).thenReturn(responseDto);

        ResponseEntity<PaymentResponseDto> response = paymentController.confirm(1L, PaymentStatus.SUCCESS);
        PaymentResponseDto result = response.getBody();

        assertThat(result.paymentId()).isEqualTo(1L);
        assertThat(result.status()).isEqualTo(PaymentStatus.SUCCESS);
    }

    @Test
    void getById_shouldReturnPayment() {
        when(paymentService.getById(1L)).thenReturn(payment);
        when(paymentConverter.toDto(any())).thenReturn(responseDto);

        ResponseEntity<PaymentResponseDto> response = paymentController.getById(1L);
        PaymentResponseDto result = response.getBody();

        assertThat(result.paymentId()).isEqualTo(1L);
    }

    @Test
    void getPaymentsByOrder_shouldReturnList() {
        when(paymentService.getAllByOrder(2L)).thenReturn(List.of(payment));
        when(paymentConverter.toDto(any())).thenReturn(responseDto);

        ResponseEntity<List<PaymentResponseDto>> response = paymentController.getPaymentsByOrder(2L);
        List<PaymentResponseDto> result = response.getBody();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).paymentId()).isEqualTo(1L);
    }
}

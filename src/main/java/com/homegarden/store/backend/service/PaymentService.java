package com.homegarden.store.backend.service;

import com.homegarden.store.backend.entity.Payment;
import com.homegarden.store.backend.enums.PaymentStatus;

import java.math.BigDecimal;

public interface PaymentService {

    Payment createPayment(Long orderId, BigDecimal amount);

    Payment confirmPayment(Long paymentId, PaymentStatus status);

    Payment getById(Long paymentId);
}


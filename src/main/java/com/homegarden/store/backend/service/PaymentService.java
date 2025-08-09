package com.homegarden.store.backend.service;

import com.homegarden.store.backend.entity.Payment;
import com.homegarden.store.backend.enums.PaymentStatus;

import java.util.List;

public interface PaymentService {

    List<Payment> getAllPayments();

    Payment create(Payment payment);

    Payment confirm(Long paymentId, PaymentStatus status);

    Payment getById(Long paymentId);

    List<Payment> getPaymentsByOrder(Long orderId);
}
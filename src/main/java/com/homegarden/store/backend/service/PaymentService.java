package com.homegarden.store.backend.service;

import com.homegarden.store.backend.entity.Order;
import com.homegarden.store.backend.entity.Payment;
import com.homegarden.store.backend.enums.PaymentStatus;

import java.util.List;

public interface PaymentService {

    Payment createPayment(Payment payment);

    Payment confirmPayment(Long paymentId, PaymentStatus status);

    Payment getById(Long paymentId);

    List<Payment> getPaymentsByOrder(Order order);
}
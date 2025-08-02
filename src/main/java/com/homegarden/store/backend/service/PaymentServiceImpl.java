package com.homegarden.store.backend.service.impl;

import com.homegarden.store.backend.entity.Order;
import com.homegarden.store.backend.entity.Payment;
import com.homegarden.store.backend.enums.PaymentStatus;
import com.homegarden.store.backend.exception.OrderNotFoundException;
import com.homegarden.store.backend.exception.PaymentNotFoundException;
import com.homegarden.store.backend.repository.PaymentRepository;
import com.homegarden.store.backend.service.OrderService;
import com.homegarden.store.backend.service.PaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderService orderService;

    public PaymentServiceImpl(PaymentRepository paymentRepository, OrderService orderService) {
        this.paymentRepository = paymentRepository;
        this.orderService = orderService;
    }

    @Override
    public Payment createPayment(Long orderId, BigDecimal amount) {
        Order order = orderService.getById(orderId);

        Payment payment = Payment.builder()
                .order(order)
                .amount(amount)
                .build();

        return paymentRepository.save(payment);
    }

    @Override
    public Payment confirmPayment(Long paymentId, PaymentStatus status) {
        Payment payment = getById(paymentId);

        payment.setStatus(status);

        if (status == PaymentStatus.SUCCESS) {
            Order order = payment.getOrder();
            order.setStatus(com.homegarden.store.backend.enums.Status.PAID);
        }

        return paymentRepository.save(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public Payment getById(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));
    }
}


package com.homegarden.store.backend.service;

import com.homegarden.store.backend.entity.Order;
import com.homegarden.store.backend.entity.Payment;
import com.homegarden.store.backend.entity.User;
import com.homegarden.store.backend.enums.PaymentStatus;
import com.homegarden.store.backend.enums.Status;
import com.homegarden.store.backend.exception.DoublePaymentException;
import com.homegarden.store.backend.exception.OrderNotFoundException;
import com.homegarden.store.backend.exception.PaymentNotFoundException;
import com.homegarden.store.backend.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderService orderService;
    private final UserService userService;

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    @Transactional
    public Payment create(Payment payment) {
        Order order = orderService.getById(payment.getOrder().getOrderId());

        User user = userService.getCurrentUser();
        if (!order.getUser().equals(user)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        }

        if (!order.getStatus().equals(Status.CREATED)) {
            throw new OrderNotFoundException("Unable create payment for order");
        }

//        if (order.getStatus() == Status.AWAITING_PAYMENT) {
//            throw new DoublePaymentException("Order id" + order.getOrderId()
//                    + " already has unpaid payment");
//        }
        List<Payment> payments = order.getPayment();
        for (Payment p : payments) {
            if (p.getStatus().equals(PaymentStatus.PENDING)) {
                throw new DoublePaymentException("Order id" + order.getOrderId()
                        + " already has unpaid payment: id" + p.getId());
            }
        }

        payment.setOrder(order);
        payment.setAmount(order.getOrderTotalSum());
        order.setStatus(Status.AWAITING_PAYMENT);

        return paymentRepository.save(payment);
    }

    @Override
    @Transactional
    public Payment confirm(Long paymentId, PaymentStatus status) {
        Payment payment = getById(paymentId);
        payment.setStatus(status);

        Order order = payment.getOrder();
        order.setStatus(status == PaymentStatus.SUCCESS ? Status.PAID : Status.CREATED);

        return paymentRepository.save(payment);
    }

    @Override
    public Payment getById(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));

        User user = userService.getCurrentUser();
        if (!payment.getOrder().getUser().equals(user)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        }

        return payment;
    }

    @Override
    public List<Payment> getAllByOrder(Long orderId) {
        Order order = orderService.getById(orderId);

        User user = userService.getCurrentUser();
        if (!order.getUser().equals(user)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        }

        return paymentRepository.findAllByOrder(order);
    }
}
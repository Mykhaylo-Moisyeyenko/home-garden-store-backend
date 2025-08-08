package com.homegarden.store.backend.service;

import com.homegarden.store.backend.entity.Order;
import com.homegarden.store.backend.entity.Payment;
import com.homegarden.store.backend.enums.PaymentStatus;
import com.homegarden.store.backend.enums.Status;
import com.homegarden.store.backend.exception.OrderNotFoundException;
import com.homegarden.store.backend.exception.PaymentNotFoundException;
import com.homegarden.store.backend.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderService orderService;

    private final AccessCheckService accessCheckService;

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    @Transactional
    public Payment create(Payment payment) {
        Order order = orderService.getById(payment.getOrder().getOrderId());

        accessCheckService.checkAccess(order);
      
        if (!order.getStatus().equals(Status.CREATED)){
                throw new OrderNotFoundException("Unable create payment for order");
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

        if (status == PaymentStatus.SUCCESS) {
            Order order = payment.getOrder();
            order.setStatus(Status.PAID);
        }
      
        return paymentRepository.save(payment);
    }

    @Override
    public Payment getById(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));
      
        accessCheckService.checkAccess(payment);
      
        return payment;
    }

    @Override
    public List<Payment> getAllByOrder(Long orderId) {
        Order order = orderService.getById(orderId);
        accessCheckService.checkAccess(order);

        return paymentRepository.findAllByOrder(order);
    }
}
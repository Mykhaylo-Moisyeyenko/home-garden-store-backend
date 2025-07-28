package com.homegarden.store.backend.service;

import com.homegarden.store.backend.exception.PaymentNotFoundException;
import com.homegarden.store.backend.model.entity.Payment;
import com.homegarden.store.backend.model.entity.PaymentStatus;
import com.homegarden.store.backend.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    public Payment create(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public List<Payment> getAll() {
        return paymentRepository.findAll();
    }

    @Override
    public Payment getById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment with id " + id + " not found"));
    }

    @Override
    public Payment updateStatus(Long id, String status) {
        Payment payment = getById(id);
        payment.setStatus(PaymentStatus.valueOf(status.toUpperCase()));
        return paymentRepository.save(payment);
    }

    @Override
    public void delete(Long id) {
        Payment payment = getById(id);
        paymentRepository.delete(payment);
    }
}


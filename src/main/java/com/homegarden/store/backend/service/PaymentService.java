package com.homegarden.store.backend.service;

import com.homegarden.store.backend.model.entity.Payment;

import java.util.List;

public interface PaymentService {

    Payment create(Payment payment);

    List<Payment> getAll();

    Payment getById(Long id);

    Payment updateStatus(Long id, String status);

    void delete(Long id);
}

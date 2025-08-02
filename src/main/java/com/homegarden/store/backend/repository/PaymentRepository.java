package com.homegarden.store.backend.repository;

import com.homegarden.store.backend.entity.Order;
import com.homegarden.store.backend.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByOrder(Order order);
}

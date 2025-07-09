package com.homegarden.store.backend.repository;

import com.homegarden.store.backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
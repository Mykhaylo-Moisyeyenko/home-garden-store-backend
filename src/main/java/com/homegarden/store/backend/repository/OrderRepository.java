package com.homegarden.store.backend.repository;

import com.homegarden.store.backend.entity.Order;
import com.homegarden.store.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> user(User user);

    List<Order> findAllByUserUserId(Long userId);
}
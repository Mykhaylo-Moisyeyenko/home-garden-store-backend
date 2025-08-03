package com.homegarden.store.backend.repository;

import com.homegarden.store.backend.entity.Order;
import com.homegarden.store.backend.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByUserUserId(Long userId);

    List<Order> findByStatusAndUpdatedAtBefore(Status status, LocalDateTime updatedAtBefore);
}
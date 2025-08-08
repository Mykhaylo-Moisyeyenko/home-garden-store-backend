package com.homegarden.store.backend.repository;

import com.homegarden.store.backend.entity.Order;
import com.homegarden.store.backend.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByUserUserId(Long userId);

    List<Order> findByStatusAndUpdatedAtAfter(Status status, LocalDateTime updatedAtAfter);

    List<Order> findByStatusAndUpdatedAtBefore(Status status, LocalDateTime updatedAtBefore);
}
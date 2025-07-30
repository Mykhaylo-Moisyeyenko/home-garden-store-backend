package com.homegarden.store.backend.utils;

import com.homegarden.store.backend.entity.Order;
import com.homegarden.store.backend.enums.Status;

import java.time.LocalDateTime;
import java.util.Optional;

public class OrderStatusCalculator {

    public static Optional<Status> findNewStatus(Order order) {
        switch (order.getStatus()) {
            case CREATED, AWAITING_PAYMENT -> {
                if (order.getUpdatedAt().isBefore(LocalDateTime.now().minusMinutes(15))) {
                    return Optional.of(Status.CANCELLED);
                } else {
                    return Optional.empty();
                }
            }
            case PAID -> {
                if (order.getUpdatedAt().isBefore(LocalDateTime.now().minusMinutes(10))) {
                    return Optional.of(Status.SHIPPED);
                } else {
                    return Optional.empty();
                }
            }
            case SHIPPED -> {
                if (order.getUpdatedAt().isBefore(LocalDateTime.now().minusMinutes(10))) {
                    return Optional.of(Status.DELIVERED);
                } else {
                    return Optional.empty();
                }
            }
            default -> {
                return Optional.empty();
            }
        }
    }
}
package com.homegarden.store.backend.utils;

import com.homegarden.store.backend.entity.Order;
import com.homegarden.store.backend.enums.Status;

public class OrderStatusChanger {

    public static Status getNext(Order order) {
        return switch (order.getStatus()) {
            case CREATED, AWAITING_PAYMENT -> Status.CANCELLED;

            case PAID -> Status.SHIPPED;

            case SHIPPED -> Status.DELIVERED;

            default -> order.getStatus();
        };
    }
}
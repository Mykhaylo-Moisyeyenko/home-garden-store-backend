package com.homegarden.store.backend.calculator;

import com.homegarden.store.backend.entity.Order;
import com.homegarden.store.backend.enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.homegarden.store.backend.utils.OrderStatusChanger.getNext;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderStatusChangerTest {

    private Order order;

    @BeforeEach
    void setUp() {
        order = Order.builder().orderId(1L).build();
    }

    @Test
    void shouldReturnCancelledIfCreatedAndOlderThan15Minutes() {
        order.setStatus(Status.CREATED);
        order.setUpdatedAt(LocalDateTime.now().minusMinutes(16));

        Status result = getNext(order);

        assertEquals(Status.CANCELLED, result);
    }

    @Test
    void shouldReturnShippedIfPaidAndOlderThan10Minutes() {
        order.setStatus(Status.PAID);
        order.setUpdatedAt(LocalDateTime.now().minusMinutes(11));

        Status result = getNext(order);

        assertEquals(Status.SHIPPED, result);
    }

    @Test
    void shouldReturnDeliveredIfShippedAndOlderThan10Minutes() {
        order.setStatus(Status.SHIPPED);
        order.setUpdatedAt(LocalDateTime.now().minusMinutes(11));

        Status result = getNext(order);

        assertEquals(Status.DELIVERED, result);
    }

    @Test
    void shouldReturnSameStatus() {
        order.setStatus(Status.DELIVERED);
        order.setUpdatedAt(LocalDateTime.now().minusMinutes(30));

        Status result = getNext(order);

        assertEquals(Status.DELIVERED, result);
    }
}
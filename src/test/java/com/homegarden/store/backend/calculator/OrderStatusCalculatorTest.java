package com.homegarden.store.backend.calculator;

import com.homegarden.store.backend.entity.Order;
import com.homegarden.store.backend.enums.Status;
import com.homegarden.store.backend.utils.OrderStatusCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class OrderStatusCalculatorTest {

    private OrderStatusCalculator calculator;
    private Order order;

    @BeforeEach
    void setUp() {
        calculator = new OrderStatusCalculator();
        order = Order.builder().orderId(1L).build();
    }

    @Test
    void shouldReturnCancelledIfCreatedAndOlderThan15Minutes() {
        order.setStatus(Status.CREATED);
        order.setUpdatedAt(LocalDateTime.now().minusMinutes(16));

        Optional<Status> result = calculator.findNewStatus(order);

        assertThat(result).contains(Status.CANCELLED);
    }

    @Test
    void shouldReturnEmptyIfCreatedAndRecent() {
        order.setStatus(Status.CREATED);
        order.setUpdatedAt(LocalDateTime.now().minusMinutes(5));

        Optional<Status> result = calculator.findNewStatus(order);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnShippedIfPaidAndOlderThan10Minutes() {
        order.setStatus(Status.PAID);
        order.setUpdatedAt(LocalDateTime.now().minusMinutes(11));

        Optional<Status> result = calculator.findNewStatus(order);

        assertThat(result).contains(Status.SHIPPED);
    }

    @Test
    void shouldReturnDeliveredIfShippedAndOlderThan10Minutes() {
        order.setStatus(Status.SHIPPED);
        order.setUpdatedAt(LocalDateTime.now().minusMinutes(11));

        Optional<Status> result = calculator.findNewStatus(order);

        assertThat(result).contains(Status.DELIVERED);
    }

    @Test
    void shouldReturnEmptyIfStatusUnhandled() {
        order.setStatus(Status.DELIVERED);
        order.setUpdatedAt(LocalDateTime.now().minusMinutes(30));

        Optional<Status> result = calculator.findNewStatus(order);

        assertThat(result).isEmpty();
    }
}

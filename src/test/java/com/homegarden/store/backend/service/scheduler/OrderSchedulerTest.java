package com.homegarden.store.backend.service.scheduler;

import com.homegarden.store.backend.entity.Order;
import com.homegarden.store.backend.enums.Status;
import com.homegarden.store.backend.service.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderSchedulerTest {

    @Mock
    private OrderServiceImpl orderService;

    @InjectMocks
    private OrderScheduler orderScheduler;

    LocalDateTime timeUpdated = LocalDateTime.now();

    Order orderCreated = Order.builder()
            .orderId(1L)
            .updatedAt(timeUpdated.minusMinutes(8))
            .build();

    Order orderAwaitingPayment = Order.builder()
            .orderId(2L)
            .updatedAt(timeUpdated)
            .build();

    Order orderPaid = Order.builder()
            .orderId(3L)
            .updatedAt(timeUpdated)
            .build();

    Order orderShipped = Order.builder()
            .orderId(4L)
            .updatedAt(timeUpdated)
            .build();

    @Test
    void processCreatedOrders() {
        when(orderService.getAllByStatusAndUpdatedAtBefore(eq(Status.CREATED),
                any(LocalDateTime.class))).thenReturn(List.of(orderCreated));

        orderScheduler.processCreatedOrders();

        verify(orderService, times(1))
                .getAllByStatusAndUpdatedAtBefore(eq(Status.CREATED), any(LocalDateTime.class));
        verify(orderService).updateStatus(orderCreated, Status.CANCELLED);
    }

    @Test
    void processAwaitingPayment() {
        when(orderService.getAllByStatusAndUpdatedAtBefore(eq(Status.AWAITING_PAYMENT),
                any(LocalDateTime.class))).thenReturn(List.of(orderAwaitingPayment));

        orderScheduler.processAwaitingPayment();

        verify(orderService, times(1))
                .getAllByStatusAndUpdatedAtBefore(eq(Status.AWAITING_PAYMENT), any(LocalDateTime.class));
        verify(orderService).updateStatus(orderAwaitingPayment, Status.CANCELLED);
    }

    @Test
    void processPaid() {
        when(orderService.getAllByStatusAndUpdatedAtAfter(eq(Status.PAID),
                any(LocalDateTime.class))).thenReturn(List.of(orderPaid));

        orderScheduler.processPaid();

        verify(orderService, times(1))
                .getAllByStatusAndUpdatedAtAfter(eq(Status.PAID), any(LocalDateTime.class));
        verify(orderService).updateStatus(orderPaid, Status.SHIPPED);
    }

    @Test
    void processShipped() {
        when(orderService.getAllByStatusAndUpdatedAtAfter(eq(Status.SHIPPED),
                any(LocalDateTime.class))).thenReturn(List.of(orderShipped));

        orderScheduler.processShipped();

        verify(orderService, times(1))
                .getAllByStatusAndUpdatedAtAfter(eq(Status.SHIPPED), any(LocalDateTime.class));
        verify(orderService).updateStatus(orderShipped, Status.DELIVERED);
    }
}
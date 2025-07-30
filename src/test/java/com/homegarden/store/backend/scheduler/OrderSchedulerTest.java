package com.homegarden.store.backend.scheduler;

import com.homegarden.store.backend.entity.Order;
import com.homegarden.store.backend.enums.Status;
import com.homegarden.store.backend.service.OrderService;
import com.homegarden.store.backend.service.scheduler.OrderScheduler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.*;

class OrderSchedulerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderScheduler orderScheduler;

    private Order order;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        order = Order.builder().orderId(1L).status(Status.CREATED).build();
    }

    @Test
    void processOrders_shouldUpdateStatusesOfPendingOrders() {
        List<Order> orders = List.of(order);
        List<Status> statuses = List.of(Status.CREATED, Status.AWAITING_PAYMENT, Status.PAID, Status.SHIPPED);

        when(orderService.getAllByStatuses(statuses)).thenReturn(orders);

        orderScheduler.processCreatedOrders();
        orderScheduler.processAwaitingPayment();
        orderScheduler.processPaid();
        orderScheduler.processShipped();

        verify(orderService).getAllByStatuses(statuses);
        verify(orderService).updateStatus(order);
    }
}

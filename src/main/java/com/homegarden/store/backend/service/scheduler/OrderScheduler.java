package com.homegarden.store.backend.service.scheduler;

import com.homegarden.store.backend.entity.Order;
import com.homegarden.store.backend.enums.Status;
import com.homegarden.store.backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderScheduler {

    private final OrderService orderService;

    @Async
    @Scheduled(cron = "${scheduler.orders.interval-in-cron}")
    public void processCreatedOrders() {
        List<Order> createdOrders = orderService
                .getAllByStatusAndUpdatedAtBefore(
                        Status.CREATED,
                        LocalDateTime.now().minusMinutes(5));

        for (Order order : createdOrders) {
            orderService.updateStatus(order, Status.CANCELLED);
        }
    }

    @Async
    @Scheduled(cron = "${scheduler.orders.interval-in-cron}")
    public void processAwaitingPayment() {
        List<Order> createdOrders = orderService
                .getAllByStatusAndUpdatedAtBefore(
                        Status.AWAITING_PAYMENT,
                        LocalDateTime.now().minusMinutes(5));

        for (Order order : createdOrders) {
            orderService.updateStatus(order, Status.CANCELLED);
        }
    }

    @Async
    @Scheduled(cron = "${scheduler.orders.interval-in-cron}")
    public void processPaid() {
        processOrderAfter(Status.PAID, Status.SHIPPED, 3);
    }

    @Async
    @Scheduled(cron = "${scheduler.orders.interval-in-cron}")
    public void processShipped() {
        processOrderAfter(Status.SHIPPED, Status.DELIVERED, 3);
    }

    private void processOrderAfter(Status oldStatus, Status newStatus, long minutes) {
        List<Order> createdOrders = orderService
                .getAllByStatusAndUpdatedAtAfter(
                        oldStatus,
                        LocalDateTime.now().minusMinutes(minutes));

        for (Order order : createdOrders) {
            orderService.updateStatus(order, newStatus);
        }
    }
}

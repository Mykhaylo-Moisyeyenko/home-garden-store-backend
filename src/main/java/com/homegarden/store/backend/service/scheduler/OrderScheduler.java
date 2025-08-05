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
    @Scheduled(cron="${scheduler.orders.interval-in-cron}")
    public void processCreatedOrders() {
        List<Order> createdOrders = orderService.getAllByStatusAndUpdatedAtBefore(Status.CREATED, LocalDateTime.now().minusMinutes(5));
        for (Order order : createdOrders) {
            orderService.updateStatus(order, Status.CANCELLED);
        }
    }

    @Async
    @Scheduled(cron="${scheduler.orders.interval-in-cron}")
    public void processAwaitingPayment() {
        List<Order> createdOrders = orderService.getAllByStatusAndUpdatedAtBefore(Status.AWAITING_PAYMENT, LocalDateTime.now().minusMinutes(5));
        for (Order order : createdOrders) {
            orderService.updateStatus(order, Status.CANCELLED);
        }
    }

    @Async
    @Scheduled(cron="${scheduler.orders.interval-in-cron}")
    public void processPaid() {
        List<Order> createdOrders = orderService.getAllByStatusAndUpdatedAtBefore(Status.PAID, LocalDateTime.now().minusMinutes(3));
        for (Order order : createdOrders) {
            orderService.updateStatus(order, Status.SHIPPED);
        }
    }

    @Async
    @Scheduled(cron="${scheduler.orders.interval-in-cron}")
    public void processShipped() {
        List<Order> createdOrders = orderService.getAllByStatusAndUpdatedAtBefore(Status.SHIPPED, LocalDateTime.now().minusMinutes(3));
        for (Order order : createdOrders) {
            orderService.updateStatus(order, Status.DELIVERED);
        }
    }
}
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
    @Scheduled(fixedDelay = 30000)
    public void processCreatedOrders() {
        List<Order> createdOrders = orderService.getAllByStatusAndUpdatedAtBefore(Status.CREATED, LocalDateTime.now().minusMinutes(5));
        for (Order order : createdOrders) {
            orderService.updateStatus(order);
        }
    }

    @Async
    @Scheduled(fixedDelay = 30000)
    public void processAwaitingPayment() {
        List<Order> createdOrders = orderService.getAllByStatusAndUpdatedAtBefore(Status.AWAITING_PAYMENT, LocalDateTime.now().minusMinutes(5));
        for (Order order : createdOrders) {
            orderService.updateStatus(order);
        }
    }

    @Async
    @Scheduled(fixedDelay = 30000)
    public void processPaid() {
        List<Order> createdOrders = orderService.getAllByStatusAndUpdatedAtBefore(Status.PAID, LocalDateTime.now().minusMinutes(5));
        for (Order order : createdOrders) {
            orderService.updateStatus(order);
        }
    }

    @Async
    @Scheduled(fixedDelay = 30000)
    public void processShipped() {
        List<Order> createdOrders = orderService.getAllByStatusAndUpdatedAtBefore(Status.SHIPPED, LocalDateTime.now().minusMinutes(5));
        for (Order order : createdOrders) {
            orderService.updateStatus(order);
        }
    }
}
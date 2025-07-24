package com.homegarden.store.backend.scheduler;

import com.homegarden.store.backend.entity.Order;
import com.homegarden.store.backend.enums.Status;
import com.homegarden.store.backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderScheduler {

    private final OrderService orderService;

    @Scheduled(fixedDelay = 30000)
    public void processOrders() {
        List<Status> statuses = List.of(Status.CREATED, Status.AWAITING_PAYMENT, Status.PAID, Status.SHIPPED);
        List<Order> pendingOrders = orderService.getAllOrdersByStatuses(statuses);
        for (Order order : pendingOrders) {
            orderService.updateStatus(order);
        }
    }
}
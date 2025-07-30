package com.homegarden.store.backend.service;

import com.homegarden.store.backend.dto.TopCancelledProductDTO;
import com.homegarden.store.backend.entity.Order;
import com.homegarden.store.backend.enums.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    Order create(Order order);

    Order getById(long id);

    List<Order> getAll();

    void updateStatus(Order order);

    void cancel(Long id);

    List<Order> getAllByUserId(Long userId);

    List<Order> getAllByStatuses(List<Status> statuses);

    List<TopCancelledProductDTO> getTopCancelledProducts();

    boolean isProductUsedInOrders(Long productId);

    List<Order> getAllByStatusAndUpdatedAtBefore(Status status, LocalDateTime updatedAtBefore);
}
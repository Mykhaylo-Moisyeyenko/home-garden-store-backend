package com.homegarden.store.backend.service;

import com.homegarden.store.backend.dto.TopCancelledProductDTO;
import com.homegarden.store.backend.entity.Order;
import com.homegarden.store.backend.enums.Status;

import java.util.List;

public interface OrderService {
    Order create(Order order);

    Order getById(long id);

    List<Order> getAll();

    void updateStatus(Order order);

    void cancelOrder(Long id);

    List<Order> getAllOrdersByUserId(Long userId);

    List<Order> getAllOrdersByStatuses(List<Status> statuses);

    List<TopCancelledProductDTO> getTopCancelledProducts();

    boolean isProductUsedInOrders(Long productId);
}
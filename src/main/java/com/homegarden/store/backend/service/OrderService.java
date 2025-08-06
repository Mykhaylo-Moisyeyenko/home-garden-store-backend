package com.homegarden.store.backend.service;

import com.homegarden.store.backend.dto.CreateOrderRequestDTO;
import com.homegarden.store.backend.dto.TopCancelledProductDTO;
import com.homegarden.store.backend.entity.Order;
import com.homegarden.store.backend.enums.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    Order create(CreateOrderRequestDTO createOrderRequestDTO);

    Order getById(long id);

    List<Order> getAll();

    void updateStatus(Order order, Status status);

    void cancel(Long id);

    List<Order> getAllByUserId(Long userId);

    List<TopCancelledProductDTO> getTopCancelledProducts();

    boolean isProductUsedInOrders(Long productId);

    List<Order> getAllByStatusAndUpdatedAtBefore(Status status, LocalDateTime updatedAtBefore);

    List<Order> getAllByStatusAndUpdatedAtAfter(Status status, LocalDateTime updatedAtAfter);
}
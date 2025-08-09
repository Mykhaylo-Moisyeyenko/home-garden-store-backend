package com.homegarden.store.backend.service;

import com.homegarden.store.backend.dto.CreateOrderRequestDto;
import com.homegarden.store.backend.dto.TopCancelledProductDto;
import com.homegarden.store.backend.entity.Order;
import com.homegarden.store.backend.enums.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {

    Order create(CreateOrderRequestDto createOrderRequestDto);

    Order getById(long id);

    List<Order> getAll();

    void updateStatus(Order order, Status status);

    void cancel(Long id);

    List<Order> getAllByUser();

    List<TopCancelledProductDto> getTopCancelledProducts();

    boolean isProductUsedInOrders(Long productId);

    List<Order> getAllByStatusAndUpdatedAtBefore(Status status, LocalDateTime updatedAtBefore);

    List<Order> getAllByStatusAndUpdatedAtAfter(Status status, LocalDateTime updatedAtAfter);
}
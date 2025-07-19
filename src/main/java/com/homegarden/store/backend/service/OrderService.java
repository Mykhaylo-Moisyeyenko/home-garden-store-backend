package com.homegarden.store.backend.service;

import com.homegarden.store.backend.dto.TopCancelledProductDTO;
import com.homegarden.store.backend.entity.Order;

import java.util.List;

public interface OrderService {
    Order create(Order order);

    Order getById(long id);

    List<Order> getAll();

    void update(Order order);

    void cancelOrder(Long Id);

    List<Order> getAllOrdersByUserId(Long Id);

    List<TopCancelledProductDTO> getTopCancelledProducts();
}
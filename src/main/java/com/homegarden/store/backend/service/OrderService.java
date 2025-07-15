package com.homegarden.store.backend.service;

import com.homegarden.store.backend.entity.Order;

import java.util.List;

public interface OrderService {
    public Order create(Order order);

    public Order getById(long id);

    public List<Order> getAll();

    public void update(Order order);
}
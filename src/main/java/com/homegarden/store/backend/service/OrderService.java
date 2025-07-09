package com.homegarden.store.backend.service;

import com.homegarden.store.backend.entity.Order;

import java.util.List;

public interface OrderService {
    public Order create(Order order);

    public Order findById(long id);

    public List<Order> findAll();

    public void delete(long id);

    public void update(Order order);
}
package com.homegarden.store.backend.service;

import com.homegarden.store.backend.entity.Order;

public interface CartToOrderService {
    Order create(Order order);
}
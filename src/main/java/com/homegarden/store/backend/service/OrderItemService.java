package com.homegarden.store.backend.service;

import java.util.List;

public interface OrderItemService {

    List<Object[]> getTopCancelledProducts();

    boolean isProductUsedInOrders(

            Long productId);
}
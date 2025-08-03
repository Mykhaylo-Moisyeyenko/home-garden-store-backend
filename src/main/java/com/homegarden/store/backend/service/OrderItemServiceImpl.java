package com.homegarden.store.backend.service;

import com.homegarden.store.backend.entity.OrderItem;
import com.homegarden.store.backend.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;

    @Override
    public void save(OrderItem orderItem) {
        orderItemRepository.save(orderItem);
    }

    public List<Object[]> getTopCancelledProducts() {
        return orderItemRepository.findTopCancelledProducts();
    }

    public boolean isProductUsedInOrders(Long productId){
        return orderItemRepository.existsByProductProductId(productId);
    }
}
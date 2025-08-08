package com.homegarden.store.backend.service;

import com.homegarden.store.backend.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;

    public List<Object[]> getTopCancelledProducts() {
        return orderItemRepository.findTopCancelledProducts();
    }

    public boolean isProductUsedInOrders(Long productId) {
        return orderItemRepository.existsByProductProductId(productId);
    }
}
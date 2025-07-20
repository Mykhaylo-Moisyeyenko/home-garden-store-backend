package com.homegarden.store.backend.service;

import com.homegarden.store.backend.dto.TopCancelledProductDTO;
import com.homegarden.store.backend.entity.Order;
import com.homegarden.store.backend.exception.OrderNotFoundException;
import com.homegarden.store.backend.exception.OrderUnableToCancelException;
import com.homegarden.store.backend.repository.OrderItemRepository;
import com.homegarden.store.backend.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.homegarden.store.backend.enums.Status.*;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public Order create(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Order getById(long id) {
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("Order with id " + id + " not found"));
    }

    @Override
    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    @Override
    public void update(Order order) {

    }

    @Override
    public List<Order> getAllOrdersByUserId(Long userId) {
        getById(userId);
        return orderRepository.findAllByUserUserId(userId);
    }

    @Override
    public void cancelOrder(Long orderId) {
        Order order = getById(orderId);
        if (order.getStatus().equals(CREATED) || order.getStatus().equals(AWAITING_PAYMENT)) {
            order.setStatus(CANCELLED);
            orderRepository.save(order);
        } else  {
            throw new OrderUnableToCancelException("Order with id " + orderId + " can't be cancelled");
        }
    }

    @Override
    public List<TopCancelledProductDTO> getTopCancelledProducts() {
        List<Object[]> data = orderItemRepository.findTopCancelledProducts();
        return data.stream()
                .map(obj -> new TopCancelledProductDTO(
                        (Long) obj[0],
                        (String) obj[1],
                        (Long) obj[2]
                ))
                .toList();
    }
}
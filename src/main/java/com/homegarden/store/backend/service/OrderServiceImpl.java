package com.homegarden.store.backend.service;

import com.homegarden.store.backend.calculator.OrderStatusCalculator;
import com.homegarden.store.backend.dto.TopCancelledProductDTO;
import com.homegarden.store.backend.entity.Order;
import com.homegarden.store.backend.enums.Status;
import com.homegarden.store.backend.exception.OrderNotFoundException;
import com.homegarden.store.backend.exception.OrderUnableToCancelException;
import com.homegarden.store.backend.repository.OrderItemRepository;
import com.homegarden.store.backend.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.homegarden.store.backend.enums.Status.*;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderStatusCalculator orderStatusCalculator;
    private final UserService userService;

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
    public void updateStatus(Order order) {
        Optional<Status> newStatus = orderStatusCalculator.findNewStatus(order);
        if (newStatus.isPresent()){
            order.setStatus(newStatus.get());
            orderRepository.save(order);
        }
    }

    @Override
    public List<Order> getAllOrdersByUserId(Long userId) {
        if (!userService.existsById(userId)) {
            throw new OrderNotFoundException("User with id " + userId + " not found");
        }
        return orderRepository.findAllByUserUserId(userId);
    }

    @Override
    public List<Order> getAllOrdersByStatuses(List<Status> statuses) {
        return orderRepository.findByStatusIn(statuses);
    }

    @Override
    public void cancelOrder(Long id) {
        Order order = getById(id);
        if (order.getStatus().equals(CREATED) || order.getStatus().equals(AWAITING_PAYMENT)) {
            order.setStatus(CANCELLED);
            orderRepository.save(order);
        } else  {
            throw new OrderUnableToCancelException("Order with id " + id + " can't be cancelled");
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

    @Override
    public boolean isProductUsedInOrders(Long productId) {
        return orderItemRepository.existsByProductProductId(productId);
    }
}
package com.homegarden.store.backend.service;

import com.homegarden.store.backend.dto.TopCancelledProductDTO;
import com.homegarden.store.backend.entity.Cart;
import com.homegarden.store.backend.entity.CartItem;
import com.homegarden.store.backend.entity.Order;
import com.homegarden.store.backend.entity.OrderItem;
import com.homegarden.store.backend.enums.Status;
import com.homegarden.store.backend.exception.OrderNotFoundException;
import com.homegarden.store.backend.exception.OrderUnableToCancelException;
import com.homegarden.store.backend.repository.OrderItemRepository;
import com.homegarden.store.backend.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.homegarden.store.backend.enums.Status.*;
import static com.homegarden.store.backend.utils.OrderStatusCalculator.findNewStatus;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserService userService;

    private final CartService cartService;
    private final OrderItemService orderItemService;

    @Override
    @Transactional
    public Order create(Order order) {
        Cart cart = cartService.getByUserId(order.getUser().getUserId());

        //список товаров в заказе
        LinkedList<OrderItem> updatedOrderItems = new LinkedList<>(order.getItems());

        //список всех товаров в корзине
        Map<Long, CartItem> cartItems = new HashMap<>();
        for (CartItem cartItem : cart.getItems()) {
            cartItems.put(cartItem.getProduct().getProductId(), cartItem);
        }

        //пропускаем товары, которых нет в корзине
        for (OrderItem orderItem : order.getItems()) {
            Long productId = orderItem.getProduct().getProductId();
            if (!cartItems.containsKey(productId)) {
                updatedOrderItems.remove(orderItem);
            }
        }

        //если корректных товаров в заказе нет и он пустой
        order.setItems(updatedOrderItems);
        if (updatedOrderItems.isEmpty()) {
            throw new IllegalArgumentException("No order items in the Order");
        }
        Order newOrder = orderRepository.save(order);

        //удаление из корзины
        for (OrderItem orderItem : updatedOrderItems) {
            Long productId = orderItem.getProduct().getProductId();
            CartItem cartItemToUpdate = cartItems.get(productId);

            //checking the quantity
            int quantity = cartItemToUpdate.getQuantity() - orderItem.getQuantity();

            if (quantity > 0) {
                // Cart has more Items than Order
                cartItemToUpdate.setQuantity(quantity);
            } else {
                // Cart has less or same Items quantity as Order
                orderItem.setQuantity(cartItemToUpdate.getQuantity());
                cart.deleteCartItem(cartItemToUpdate);
            }
            orderItemService.save(orderItem);
        }
        cartService.update(cart);
        return newOrder;
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
        Optional<Status> newStatus = findNewStatus(order);
        if (newStatus.isPresent()) {
            order.setStatus(newStatus.get());
            orderRepository.save(order);
        }
    }

    @Override
    public List<Order> getAllByUserId(Long userId) {
        if (!userService.existsById(userId)) {
            throw new OrderNotFoundException("User with id " + userId + " not found");
        }
        return orderRepository.findAllByUserUserId(userId);
    }

    @Override
    public List<Order> getAllByStatuses(List<Status> statuses) {
        return orderRepository.findByStatusIn(statuses);
    }

    @Override
    public void cancel(Long id) {
        Order order = getById(id);
        if (order.getStatus().equals(CREATED) || order.getStatus().equals(AWAITING_PAYMENT)) {
            order.setStatus(CANCELLED);
            orderRepository.save(order);
        } else {
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
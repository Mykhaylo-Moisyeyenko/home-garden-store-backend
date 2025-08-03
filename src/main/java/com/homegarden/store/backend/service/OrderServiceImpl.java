package com.homegarden.store.backend.service;

import com.homegarden.store.backend.dto.CreateOrderItemRequestDTO;
import com.homegarden.store.backend.dto.CreateOrderRequestDTO;
import com.homegarden.store.backend.dto.TopCancelledProductDTO;
import com.homegarden.store.backend.entity.*;
import com.homegarden.store.backend.enums.Status;
import com.homegarden.store.backend.exception.OrderItemsListIsEmptyException;
import com.homegarden.store.backend.exception.OrderNotFoundException;
import com.homegarden.store.backend.exception.OrderUnableToCancelException;
import com.homegarden.store.backend.repository.OrderItemRepository;
import com.homegarden.store.backend.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static com.homegarden.store.backend.enums.Status.*;
import static com.homegarden.store.backend.utils.OrderStatusChanger.getNext;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserService userService;

    private final CartService cartService;

    @Override
    @Transactional
    public Order create(CreateOrderRequestDTO createOrderRequestDTO) {

        Cart cart = cartService.getByUserId(1L);

        Order order = Order.builder()
                .user(User.builder().userId(1L).build())
                .deliveryAddress(createOrderRequestDTO.deliveryAddress())
                .contactPhone("")
                .deliveryMethod(createOrderRequestDTO.deliveryMethod())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        List<OrderItem> orderItems = new ArrayList<>();

        Map<Long, CartItem> cartItems = new HashMap<>();
        for (CartItem cartItem : cart.getItems()) {
            cartItems.put(cartItem.getProduct().getProductId(), cartItem);
        }

        for (CreateOrderItemRequestDTO dto : createOrderRequestDTO.orderItems()) {
            Long productId = dto.productId();
            if (cartItems.containsKey(productId)) {

                CartItem cartItem = cartItems.get(dto.productId());
                Product product = cartItem.getProduct();

                int quantity = dto.quantity();

                if (cartItem.getQuantity() - dto.quantity() > 0) {
                    cartItem.setQuantity(cartItem.getQuantity() - dto.quantity());
                } else {
                    quantity = cartItem.getQuantity();
                    cart.getItems().remove(cartItem);
                }

                OrderItem orderItem = OrderItem.builder()
                        .product(product)
                        .quantity(quantity)
                        .priceAtPurchase(product.getDiscountPrice() != null ? product.getDiscountPrice() : product.getPrice())
                        .order(order)
                        .build();

                orderItems.add(orderItem);
            }
        }

        if (orderItems.isEmpty()) {
            throw new OrderItemsListIsEmptyException("Cannot create order: Products must be in the cart");
        } else {
            BigDecimal orderTotalSum = orderItems.stream()
                    .map(orderItem -> orderItem.getPriceAtPurchase()
                            .multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            order.setOrderTotalSum(orderTotalSum);
            order.setItems(orderItems);
        }
        cartService.update(cart);
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
        Status newStatus = getNext(order);
        order.setStatus(newStatus);
        orderRepository.save(order);
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
    public List<Order> getAllByStatusAndUpdatedAtBefore(Status status, LocalDateTime updatedAtBefore) {
        return orderRepository.findByStatusAndUpdatedAtBefore(status, updatedAtBefore);
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
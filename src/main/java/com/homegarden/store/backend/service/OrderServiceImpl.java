package com.homegarden.store.backend.service;

import com.homegarden.store.backend.dto.CreateOrderItemRequestDto;
import com.homegarden.store.backend.dto.CreateOrderRequestDto;
import com.homegarden.store.backend.dto.TopCancelledProductDto;
import com.homegarden.store.backend.entity.*;
import com.homegarden.store.backend.enums.Status;
import com.homegarden.store.backend.exception.OrderItemsListIsEmptyException;
import com.homegarden.store.backend.exception.OrderNotFoundException;
import com.homegarden.store.backend.exception.OrderUnableToCancelException;
import com.homegarden.store.backend.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.homegarden.store.backend.enums.Status.*;

@RequiredArgsConstructor
@Service

public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemService orderItemService;
    private final UserService userService;
    private final CartService cartService;

    private final AccessCheckService accessCheckService;

    @Override
    @Transactional
    public Order create(CreateOrderRequestDto createOrderRequestDto) {

        User user = userService.getByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        Cart cart = cartService.getByUserId(user.getUserId());

        Order order = Order.builder()
                .user(user)
                .deliveryAddress(createOrderRequestDto.deliveryAddress())
                .contactPhone("")
                .deliveryMethod(createOrderRequestDto.deliveryMethod())
                .build();

        List<OrderItem> orderItems = new ArrayList<>();

        Map<Long, CartItem> cartItems = new HashMap<>();

        for (CartItem cartItem : cart.getItems()) {
            cartItems.put(cartItem.getProduct().getProductId(), cartItem);
        }

        for (CreateOrderItemRequestDto dto : createOrderRequestDto.orderItems()) {
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

                OrderItem orderItem = OrderItem
                        .builder()
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

            BigDecimal orderTotalSum = orderItems
                    .stream()
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
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order with id " + id + " not found"));

        accessCheckService.checkAccess(order);

        return order;
    }

    @Override
    public List<Order> getAll() {

        return orderRepository.findAll();
    }

    @Override
    public void updateStatus(
            Order order,
            Status status) {

        order.setStatus(status);
        orderRepository.save(order);
    }

    @Override
    public List<Order> getAllByUserId(Long userId) {
        if (!userService.existsById(userId)) {

            throw new OrderNotFoundException("User with id " + userId + " not found");
        }
      
        User user = userService.getById(userId);
        accessCheckService.checkAccess(user);

        return orderRepository.findAllByUserUserId(userId);
    }

    @Override
    public List<Order> getAllByStatusAndUpdatedAtAfter(
            Status status,
            LocalDateTime updatedAtAfter) {

        return orderRepository.findByStatusAndUpdatedAtAfter(
                status,
                updatedAtAfter);
    }

    @Override
    public List<Order> getAllByStatusAndUpdatedAtBefore
            (Status status,
             LocalDateTime updatedAtBefore) {

        return orderRepository.findByStatusAndUpdatedAtBefore(
                status,
                updatedAtBefore);
    }

    @Override
    public void cancel(Long id) {
        Order order = getById(id);

        if (!order.getStatus().equals(CREATED) && !order.getStatus().equals(AWAITING_PAYMENT)) {

            throw new OrderUnableToCancelException("Order with id " + id + " can't be cancelled");
        }

        updateStatus(order, CANCELLED);
    }

    @Override
    public List<TopCancelledProductDto> getTopCancelledProducts() {
        List<Object[]> data = orderItemService.getTopCancelledProducts();

        return data
                .stream()
                .map(obj -> new TopCancelledProductDto(
                        (Long) obj[0],
                        (String) obj[1],
                        (Long) obj[2]
                ))
                .toList();
    }

    @Override
    public boolean isProductUsedInOrders(Long productId) {
        return orderItemService.isProductUsedInOrders(productId);
    }
}

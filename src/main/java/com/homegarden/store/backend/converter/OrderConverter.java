package com.homegarden.store.backend.converter;

import com.homegarden.store.backend.dto.CreateOrderItemRequestDTO;
import com.homegarden.store.backend.dto.CreateOrderRequestDTO;
import com.homegarden.store.backend.dto.OrderItemResponseDTO;
import com.homegarden.store.backend.dto.OrderResponseDTO;
import com.homegarden.store.backend.entity.Order;
import com.homegarden.store.backend.entity.OrderItem;
import com.homegarden.store.backend.entity.Product;
import com.homegarden.store.backend.entity.User;
import com.homegarden.store.backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderConverter implements Converter<Order, CreateOrderRequestDTO, OrderResponseDTO> {

    private final ProductService productService;

    @Override
    public Order toEntity(CreateOrderRequestDTO createOrderRequestDTO) {
        Order order = Order.builder()
                //userId, contactPhone - получаем из аутентификации, пока - заглушка 1L
                .user(User.builder().userId(1L).build())
                .deliveryAddress(createOrderRequestDTO.deliveryAddress())
                .contactPhone("")
                .deliveryMethod(createOrderRequestDTO.deliveryMethod())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        List<OrderItem> items = new ArrayList<>();

        for (CreateOrderItemRequestDTO dto: createOrderRequestDTO.orderItems()){
            Product product = productService.getById(dto.productId());

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(dto.quantity());
            orderItem.setPriceAtPurchase(product.getDiscountPrice() != null ? product.getDiscountPrice() : product.getPrice());
            orderItem.setOrder(order);

            items.add(orderItem);
        }

        order.setItems(items);

        return order;
    }

    @Override
    public OrderResponseDTO toDto(Order order) {
        List<OrderItemResponseDTO> items = order.getItems().stream()
                .map(orderItem -> OrderItemResponseDTO.builder()
                        .orderId(order.getOrderId())
                        .productId(orderItem.getProduct().getProductId())
                        .quantity(orderItem.getQuantity())
                        .priceAtPurchase(orderItem.getPriceAtPurchase())
                        .build())
                .toList();

        OrderResponseDTO orderResponseDTO = OrderResponseDTO.builder()
                .orderId(order.getOrderId())
                .userId(order.getUser().getUserId())
                .status(order.getStatus())
                .items(items)
                .contactPhone(order.getContactPhone())
                .deliveryAddress(order.getDeliveryAddress())
                .deliveryMethod(order.getDeliveryMethod())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();

        return orderResponseDTO;
    }
}
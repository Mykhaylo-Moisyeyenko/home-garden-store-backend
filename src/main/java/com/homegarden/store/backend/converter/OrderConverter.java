package com.homegarden.store.backend.converter;

import com.homegarden.store.backend.dto.OrderItemResponseDTO;
import com.homegarden.store.backend.dto.OrderResponseDTO;
import com.homegarden.store.backend.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderConverter {

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
package com.homegarden.store.backend.converter;

import com.homegarden.store.backend.dto.OrderItemResponseDto;
import com.homegarden.store.backend.dto.OrderResponseDto;
import com.homegarden.store.backend.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor

public class OrderConverter {

    public OrderResponseDto toDto(Order order) {

        List<OrderItemResponseDto> items = order
                .getItems()
                .stream()
                .map(orderItem -> OrderItemResponseDto
                        .builder()
                        .orderId(order.getOrderId())
                        .productId(orderItem.getProduct().getProductId())
                        .quantity(orderItem.getQuantity())
                        .priceAtPurchase(orderItem.getPriceAtPurchase())
                        .build())
                .toList();

        OrderResponseDto orderResponseDTO = OrderResponseDto
                .builder()
                .orderId(order.getOrderId())
                .userId(order.getUser().getUserId())
                .status(order.getStatus())
                .items(items)
                .contactPhone(order.getContactPhone())
                .deliveryAddress(order.getDeliveryAddress())
                .deliveryMethod(order.getDeliveryMethod())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .totalSum(order.getOrderTotalSum())
                .build();

        return orderResponseDTO;
    }
}
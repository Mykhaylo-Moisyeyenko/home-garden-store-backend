package com.homegarden.store.backend.converter;

import com.homegarden.store.backend.dto.CreateOrderRequestDTO;
import com.homegarden.store.backend.dto.OrderResponseDTO;
import com.homegarden.store.backend.entity.Order;
import com.homegarden.store.backend.entity.User;
import org.springframework.stereotype.Component;

@Component
public class OrderConverter implements Converter<Order, CreateOrderRequestDTO, OrderResponseDTO> {
    @Override
    public Order toEntity(CreateOrderRequestDTO createOrderRequestDTO) {
        return Order.builder()
                //userId, contactPhone - получаем из аутентификации, пока - заглушки
                .user(User.builder().build())
                .deliveryAddress(createOrderRequestDTO.deliveryAddress())
                .contactPhone("")
                .deliveryMethod(createOrderRequestDTO.deliveryMethod())
                .build();
    }

    @Override
    public OrderResponseDTO toDto(Order order) {
        return new OrderResponseDTO(order.getStatus());
    }
}
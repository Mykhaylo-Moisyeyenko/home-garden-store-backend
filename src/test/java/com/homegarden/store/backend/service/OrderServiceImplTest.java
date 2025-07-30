package com.homegarden.store.backend.service;

import com.homegarden.store.backend.utils.OrderStatusCalculator;
import com.homegarden.store.backend.dto.TopCancelledProductDTO;
import com.homegarden.store.backend.entity.Order;
import com.homegarden.store.backend.enums.Status;
import com.homegarden.store.backend.exception.OrderNotFoundException;
import com.homegarden.store.backend.exception.OrderUnableToCancelException;
import com.homegarden.store.backend.repository.OrderItemRepository;
import com.homegarden.store.backend.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private OrderStatusCalculator orderStatusCalculator;

    @Mock
    private UserService userService;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order order;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        order = Order.builder().orderId(1L).status(Status.CREATED).build();
    }

//    @Test
//    void testCreate() {
//        when(orderRepository.save(order)).thenReturn(order);
//        Order saved = orderService.create(order);
//        assertThat(saved).isEqualTo(order);
//    }

    @Test
    void testGetByIdFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        Order result = orderService.getById(1L);
        assertThat(result).isEqualTo(order);
    }

    @Test
    void testGetByIdNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> orderService.getById(1L))
                .isInstanceOf(OrderNotFoundException.class);
    }

    @Test
    void testGetAll() {
        when(orderRepository.findAll()).thenReturn(List.of(order));
        List<Order> result = orderService.getAll();
        assertThat(result).containsExactly(order);
    }

//    @Test
//    void testUpdateStatusPresent() {
//        when(orderStatusCalculator.findNewStatus(order)).thenReturn(Optional.of(Status.SHIPPED));
//        orderService.updateStatus(order);
//        assertThat(order.getStatus()).isEqualTo(Status.SHIPPED);
//        verify(orderRepository).save(order);
//    }
//
//    @Test
//    void testUpdateStatusNotPresent() {
//        when(orderStatusCalculator.findNewStatus(order)).thenReturn(Optional.empty());
//        orderService.updateStatus(order);
//        verify(orderRepository, never()).save(order);
//    }

    @Test
    void testGetAllOrdersByUserId_UserExists() {
        when(userService.existsById(1L)).thenReturn(true);
        when(orderRepository.findAllByUserUserId(1L)).thenReturn(List.of(order));
        List<Order> result = orderService.getAllByUserId(1L);
        assertThat(result).containsExactly(order);
    }

    @Test
    void testGetAllOrdersByUserId_UserNotFound() {
        when(userService.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> orderService.getAllByUserId(1L))
                .isInstanceOf(OrderNotFoundException.class);
    }

    @Test
    void testGetAllOrdersByStatuses() {
        List<Status> statuses = List.of(Status.CREATED);
        when(orderRepository.findByStatusIn(statuses)).thenReturn(List.of(order));
        List<Order> result = orderService.getAllByStatuses(statuses);
        assertThat(result).containsExactly(order);
    }

    @Test
    void testCancelOrderAllowed() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        orderService.cancel(1L);
        assertThat(order.getStatus()).isEqualTo(Status.CANCELLED);
        verify(orderRepository).save(order);
    }

    @Test
    void testCancelOrderNotAllowed() {
        order.setStatus(Status.SHIPPED);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        assertThatThrownBy(() -> orderService.cancel(1L))
                .isInstanceOf(OrderUnableToCancelException.class);
    }

    @Test
    void testGetTopCancelledProducts() {
        Object[] data = new Object[]{1L, "Product Name", 5L};
        when(orderItemRepository.findTopCancelledProducts()).thenReturn(List.<Object[]>of(data));
        List<TopCancelledProductDTO> result = orderService.getTopCancelledProducts();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).productId()).isEqualTo(1L);
    }

    @Test
    void testIsProductUsedInOrders() {
        when(orderItemRepository.existsByProductProductId(1L)).thenReturn(true);
        boolean used = orderService.isProductUsedInOrders(1L);
        assertThat(used).isTrue();
    }
}

package com.homegarden.store.backend.service;

import com.homegarden.store.backend.dto.TopCancelledProductDto;
import com.homegarden.store.backend.entity.Order;
import com.homegarden.store.backend.entity.User;
import com.homegarden.store.backend.enums.Role;
import com.homegarden.store.backend.enums.Status;
import com.homegarden.store.backend.exception.OrderNotFoundException;
import com.homegarden.store.backend.exception.OrderUnableToCancelException;
import com.homegarden.store.backend.exception.UserNotFoundException;
import com.homegarden.store.backend.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserService userService;

    @Mock
    private OrderItemService orderItemService;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order order;
    private User user;

    @BeforeEach
    void setUp() {
        order = Order.builder().orderId(1L).status(Status.CREATED).build();
        user = User.builder().userId(1L).role(Role.ROLE_USER).build();
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
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void testGetByIdNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> orderService.getById(1L))
                .isInstanceOf(OrderNotFoundException.class);
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAll() {
        when(orderRepository.findAll()).thenReturn(List.of(order));
        List<Order> result = orderService.getAll();
        assertThat(result).containsExactly(order);
    }

//    @Test
//    void testUpdateStatusPresent() {
//        when(orderStatusChanger.getNext(order)).thenReturn(Optional.of(Status.SHIPPED));
//        orderService.updateStatus(order);
//        assertThat(order.getStatus()).isEqualTo(Status.SHIPPED);
//        verify(orderRepository).save(order);
//    }
//
//    @Test
//    void testUpdateStatusNotPresent() {
//        when(orderStatusChanger.getNext(order)).thenReturn(Optional.empty());
//        orderService.updateStatus(order);
//        verify(orderRepository, never()).save(order);
//    }

    @Test
    void testGetAllOrdersByUser_UserExists() {
        when(userService.getById(1L)).thenReturn(user);
        when(orderRepository.findAllByUser(user)).thenReturn(List.of(order));

        List<Order> result = orderService.getAllByUser();

        assertThat(result).containsExactly(order);
        verify(orderRepository, times(1)).findAllByUser(user);
    }

    @Test
    void testGetAllOrdersByUser_UserNotFound() {
        when(userService.getById(1L))
                .thenThrow(new UserNotFoundException("User not found"));

        assertThatThrownBy(() -> orderService.getAllByUser())
                .isInstanceOf(UserNotFoundException.class);
        verify(orderRepository, never()).findAllByUser(user);
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
        when(orderItemService.getTopCancelledProducts()).thenReturn(List.<Object[]>of(data));
        List<TopCancelledProductDto> result = orderService.getTopCancelledProducts();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).productId()).isEqualTo(1L);
    }

    @Test
    void testIsProductUsedInOrders() {
        when(orderItemService.isProductUsedInOrders(1L)).thenReturn(true);
        boolean used = orderService.isProductUsedInOrders(1L);
        assertThat(used).isTrue();
    }
}
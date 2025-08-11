package com.homegarden.store.backend.service;

import com.homegarden.store.backend.entity.Order;
import com.homegarden.store.backend.entity.User;
import com.homegarden.store.backend.enums.Role;
import com.homegarden.store.backend.enums.Status;
import com.homegarden.store.backend.exception.OrderNotFoundException;
import com.homegarden.store.backend.exception.OrderUnableToCancelException;
import com.homegarden.store.backend.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
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
    @Spy
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
        doReturn(user).when(userService).getCurrentUser();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        order.setUser(user);

        Order result = orderService.getById(1L);

        assertThat(result).isEqualTo(order);
        verify(userService, times(1)).getCurrentUser();
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void testGetByIdNotFound() {
        doReturn(user).when(userService).getCurrentUser();
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.getById(1L))
                .isInstanceOf(OrderNotFoundException.class);

        verify(userService, times(1)).getCurrentUser();
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAll() {
        when(orderRepository.findAll()).thenReturn(List.of(order));
        List<Order> result = orderService.getAll();
        assertThat(result).containsExactly(order);
    }


    @Test
    void testUpdateStatusSuccess() {
        Order orderChanged = Order.builder().orderId(1L).status(Status.DELIVERED).build();
        when(orderRepository.save(order)).thenReturn(orderChanged);

        orderService.updateStatus(order, Status.DELIVERED);

        assertThat(orderChanged.getStatus()).isEqualTo(Status.DELIVERED);
        verify(orderRepository, times(1)).save(order);

    }

    @Test
    void testGetAllOrdersByUser_UserExists() {
        doReturn(user).when(userService).getCurrentUser();
        when(orderRepository.findAllByUser(user)).thenReturn(List.of(order));

        List<Order> result = orderService.getAllByUser();

        assertThat(result).containsExactly(order);
        verify(userService, times(1)).getCurrentUser();
        verify(orderRepository, times(1)).findAllByUser(user);
    }

    @Test
    void testCancelOrderAllowed() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        doReturn(user).when(userService).getCurrentUser();
        order.setUser(user);
        doCallRealMethod().when(orderService).updateStatus(order, Status.CANCELLED);
        Order orderCancelled = Order.builder().orderId(1L).status(Status.CANCELLED).build();
        when(orderRepository.save(order)).thenReturn(orderCancelled);

        orderService.cancel(1L);

        assertThat(orderCancelled.getStatus()).isEqualTo(Status.CANCELLED);
        verify(orderRepository, times(1)).findById(1L);
        verify(userService, times(2)).getCurrentUser();
        verify(orderService, times(1)).updateStatus(order, Status.CANCELLED);
    }

    @Test
    void testCancelOrderNotAllowed() {
        order.setStatus(Status.SHIPPED);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        doReturn(user).when(userService).getCurrentUser();
        order.setUser(user);

        assertThatThrownBy(() -> orderService.cancel(1L))
                .isInstanceOf(OrderUnableToCancelException.class);
        verify(orderService,never()).updateStatus(any(), any());
    }

    @Test
    void testIsProductUsedInOrders() {
        when(orderItemService.isProductUsedInOrders(1L)).thenReturn(true);
        boolean used = orderService.isProductUsedInOrders(1L);
        assertThat(used).isTrue();
    }
}
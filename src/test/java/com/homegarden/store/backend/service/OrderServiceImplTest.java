package com.homegarden.store.backend.service;

import com.homegarden.store.backend.dto.CreateOrderItemRequestDTO;
import com.homegarden.store.backend.dto.CreateOrderRequestDTO;
import com.homegarden.store.backend.dto.TopCancelledProductDTO;
import com.homegarden.store.backend.entity.*;
import com.homegarden.store.backend.enums.Status;
import com.homegarden.store.backend.exception.OrderNotFoundException;
import com.homegarden.store.backend.exception.OrderUnableToCancelException;
import com.homegarden.store.backend.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
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

    @Mock
    private CartService cartService;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order order;

    @BeforeEach
    void setUp() {
        order = Order.builder().orderId(1L).status(Status.CREATED).build();
    }

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
    void testGetTopCancelledProducts() {
        Object[] data = new Object[]{1L, "Product Name", 5L};
        when(orderItemService.getTopCancelledProducts()).thenReturn(List.<Object[]>of(data));
        List<TopCancelledProductDTO> result = orderService.getTopCancelledProducts();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).productId()).isEqualTo(1L);
    }

    @Test
    void testIsProductUsedInOrders() {
        when(orderItemService.isProductUsedInOrders(1L)).thenReturn(true);
        boolean used = orderService.isProductUsedInOrders(1L);
        assertThat(used).isTrue();
    }

    @Test
    void testUpdateStatusFromAwaitingPaymentToCancelled() {
        User user = User.builder().userId(1L).build();
        Product product = Product.builder()
                .productId(1L)
                .price(new BigDecimal("100.00"))
                .build();

        OrderItem item = OrderItem.builder()
                .product(product)
                .quantity(2)
                .build();

        Order testOrder = Order.builder()
                .orderId(10L)
                .user(user)
                .status(Status.AWAITING_PAYMENT)
                .items(new ArrayList<>(List.of(item)))
                .build();

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        orderService.updateStatus(testOrder);

        assertThat(testOrder.getStatus()).isEqualTo(Status.CANCELLED);
        verify(orderRepository).save(testOrder);
    }

    @Test
    void testCreate() {
        Product product = Product.builder()
                .productId(1L)
                .price(new BigDecimal("100.00"))
                .build();

        CartItem cartItem = CartItem.builder()
                .product(product)
                .quantity(2)
                .build();

        Cart cart = Cart.builder()
                .user(User.builder().userId(1L).build())
                .items(new ArrayList<>(List.of(cartItem)))
                .build();

        CreateOrderItemRequestDTO itemDTO = new CreateOrderItemRequestDTO(1L, 2);
        CreateOrderRequestDTO requestDTO = new CreateOrderRequestDTO(
                new ArrayList<>(List.of(itemDTO)),
                "Test Address",
                "Courier"
        );

        when(cartService.getByUserId(1L)).thenReturn(cart);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order created = orderService.create(requestDTO);

        assertThat(created.getItems()).hasSize(1);
        assertThat(created.getOrderTotalSum()).isEqualTo(new BigDecimal("200.00"));
        assertThat(created.getDeliveryAddress()).isEqualTo("Test Address");
        assertThat(created.getDeliveryMethod()).isEqualTo("Courier");
    }
}


package com.homegarden.store.backend.service;

import com.homegarden.store.backend.dto.CreateOrderItemRequestDto;
import com.homegarden.store.backend.dto.CreateOrderRequestDto;
import com.homegarden.store.backend.entity.*;
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
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private CartService cartService;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order order;
    private User user;
    private Product product;
    private CartItem cartItem;
    private Cart cart;
    private CreateOrderItemRequestDto createOrderItemRequestDto;
    private CreateOrderRequestDto createOrderRequestDto;
    private Order order2;
    private OrderItem orderItem;
    private List<OrderItem> orderItems;

    @BeforeEach
    void setUp() {
        order = Order.builder().orderId(1L).status(Status.CREATED).build();
        user = User.builder().userId(1L).cart(cart).phoneNumber("1111111").role(Role.ROLE_USER).build();

        product = Product.builder().productId(1L).price(new BigDecimal("100.00")).build();
        cartItem = CartItem.builder().cartItemId(1L).cart(cart).product(product).quantity(10).build();
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(cartItem);
        cart = Cart.builder().user(user).items(cartItems).build();

        createOrderItemRequestDto = new CreateOrderItemRequestDto(1L, 5);
        List<CreateOrderItemRequestDto> items = new ArrayList<>(List.of(createOrderItemRequestDto));
        createOrderRequestDto = new CreateOrderRequestDto(items,
                "delivery address",
                "delivery method");

        order2 = Order.builder()
                .user(user)
                .deliveryAddress(createOrderRequestDto.deliveryAddress())
                .contactPhone(user.getPhoneNumber())
                .deliveryMethod(createOrderRequestDto.deliveryMethod())
                .status(Status.CREATED)
                .updatedAt(LocalDateTime.now().minusMinutes(5))
                .build();
        orderItem = OrderItem.builder()
                .product(product)
                .quantity(5)
                .priceAtPurchase(product.getDiscountPrice() != null ? product.getDiscountPrice() : product.getPrice())
                .order(order2)
                .build();
        orderItems = List.of(orderItem);
        order2.setOrderTotalSum(new BigDecimal("500.00"));
        order2.setItems(orderItems);
    }


    @Test
    void createTest() {
        when(userService.getCurrentUser()).thenReturn(user);
        when(cartService.getByUser(user)).thenReturn(cart);
        cart.getItems().get(0).setQuantity(5);
        when(cartService.update(cart)).thenReturn(cart);
        when(orderRepository.save(order2)).thenReturn(order2);

        Order actual = orderService.create(createOrderRequestDto);

        assertThat(actual).isEqualTo(order2);
        assertThat(actual.getOrderTotalSum()).isEqualTo(order2.getOrderTotalSum());
        assertThat(actual.getItems()).hasSize(orderItems.size());
        assertThat(actual.getItems().get(0).getQuantity()).isEqualTo(5);
    }

    @Test
    void testGetByIdFound() {
        doReturn(user).when(userService).getCurrentUser();
        when(orderRepository.findByOrderIdAndUser(1L, user))
                .thenReturn(Optional.of(order));

        Order result = orderService.getById(1L);

        assertThat(result).isEqualTo(order);
        verify(userService, times(1)).getCurrentUser();
        verify(orderRepository, times(1)).findByOrderIdAndUser(1L, user);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    void testGetByIdNotFound() {
        doReturn(user).when(userService).getCurrentUser();
        when(orderRepository.findByOrderIdAndUser(1L, user)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.getById(1L))
                .isInstanceOf(OrderNotFoundException.class);

        verify(userService, times(1)).getCurrentUser();
        verify(orderRepository, times(1)).findByOrderIdAndUser(1L, user);
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
        doReturn(user).when(userService).getCurrentUser();
        when(orderRepository.findByOrderIdAndUser(1L, user)).thenReturn(Optional.of(order));

        Order orderCancelled = Order.builder().orderId(1L).status(Status.CANCELLED).build();
        when(orderRepository.save(order)).thenReturn(orderCancelled);

        orderService.cancel(1L);

        assertThat(orderCancelled.getStatus()).isEqualTo(Status.CANCELLED);
        verify(userService, times(1)).getCurrentUser();
        verify(orderRepository, times(1)).findByOrderIdAndUser(1L, user);
    }

    @Test
    void testCancelOrderNotAllowed() {
        doReturn(user).when(userService).getCurrentUser();
        order.setStatus(Status.SHIPPED);
        when(orderRepository.findByOrderIdAndUser(1L, user)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.cancel(1L))
                .isInstanceOf(OrderUnableToCancelException.class);
        verify(orderRepository, never()).save(any());
    }

    @Test
    void getAllByStatusAndUpdatedAtAfter() {
        LocalDateTime time = LocalDateTime.now().minusMinutes(10);
        when(orderRepository.findByStatusAndUpdatedAtAfter(Status.CREATED, time))
                .thenReturn(List.of(order2));

        List<Order> actual = orderService.getAllByStatusAndUpdatedAtAfter(
                Status.CREATED,
                time);
        assertThat(actual).containsExactly(order2);
        verify(orderRepository).findByStatusAndUpdatedAtAfter(any(Status.class), any(LocalDateTime.class));
    }

    @Test
    void getAllByStatusAndUpdatedAtBefore() {
        LocalDateTime time = LocalDateTime.now().minusMinutes(1);
        when(orderRepository.findByStatusAndUpdatedAtAfter(Status.CREATED, time))
                .thenReturn(List.of(order2));

        List<Order> actual = orderService.getAllByStatusAndUpdatedAtAfter(
                Status.CREATED,
                time);
        assertThat(actual).containsExactly(order2);
        verify(orderRepository).findByStatusAndUpdatedAtAfter(any(Status.class), any(LocalDateTime.class));
    }

    @Test
    void getGroupedRevenueTest() {
        LocalDateTime start = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2025, 1, 12, 0, 0, 0);
        String timeCut = "month";

        List<Object[]> expected = List.<Object[]>of(
                new Object[]{LocalDate.of(2025, 1, 1), BigDecimal.valueOf(2000)});

        when(orderRepository.findGroupedRevenue(start, end, timeCut)).thenReturn(expected);

        List<Object[]> actual = orderService.getGroupedRevenue(start, end, timeCut);
        assertThat(actual).isEqualTo(expected);
        verify(orderRepository).findGroupedRevenue(start, end, timeCut);
    }
}
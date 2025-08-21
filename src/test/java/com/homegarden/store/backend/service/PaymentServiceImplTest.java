package com.homegarden.store.backend.service;

import com.homegarden.store.backend.entity.Order;
import com.homegarden.store.backend.entity.Payment;
import com.homegarden.store.backend.entity.User;
import com.homegarden.store.backend.enums.PaymentStatus;
import com.homegarden.store.backend.enums.Status;
import com.homegarden.store.backend.exception.DoublePaymentException;
import com.homegarden.store.backend.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderService orderService;

    @Mock
    private UserService userService;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPayment_shouldSetAmountAndStatus() {
        Long orderId = 1L;
        BigDecimal total = BigDecimal.valueOf(1000);

        Order order = Order.builder()
                .orderId(orderId)
                .status(Status.CREATED)
                .orderTotalSum(total)
                .build();

        Payment payment = Payment.builder()
                .order(Order.builder().orderId(orderId).build())
                .build();

        when(orderService.getById(orderId)).thenReturn(order);
        when(paymentRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Payment saved = paymentService.create(payment);

        assertThat(saved.getAmount()).isEqualTo(total);
        assertThat(saved.getOrder().getStatus()).isEqualTo(Status.AWAITING_PAYMENT);

        verify(paymentRepository).save(any());
    }

    @Test
    void createPayment_shouldThrowIfOrderHasUnpaidPayment() {
        Order order = Order.builder()
                .orderId(1L)
                .status(Status.AWAITING_PAYMENT)
                .build();

        Payment payment = Payment.builder()
                .order(order)
                .build();

        when(orderService.getById(1L)).thenReturn(order);

        assertThatThrownBy(() -> paymentService.create(payment))
                .isInstanceOf(DoublePaymentException.class);

        verify(paymentRepository, never()).save(any());
    }

    @Test
    void getAllPayments_shouldReturnList() {
        List<Payment> payments = List.of(new Payment(), new Payment());
        when(paymentRepository.findAll()).thenReturn(payments);

        List<Payment> result = paymentService.getAllPayments();

        assertThat(result).hasSize(2);
        verify(paymentRepository).findAll();
    }

    @Test
    void confirm_shouldUpdateStatusAndOrder() {
        Long paymentId = 5L;
        PaymentStatus newStatus = PaymentStatus.SUCCESS;

        User user = new User();
        Order order = Order.builder()
                .status(Status.CREATED)
                .user(user)
                .build();

        Payment payment = Payment.builder()
                .id(paymentId)
                .status(PaymentStatus.PENDING)
                .order(order)
                .build();

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
        when(userService.getCurrentUser()).thenReturn(user);
        when(paymentRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Payment updated = paymentService.confirm(paymentId, newStatus);

        assertThat(updated.getStatus()).isEqualTo(newStatus);
        assertThat(updated.getOrder().getStatus()).isEqualTo(Status.PAID);

        verify(paymentRepository).save(payment);
    }

    @Test
    void getById_shouldReturnPaymentIfUserMatches() {
        Long paymentId = 3L;
        User user = new User();
        Order order = Order.builder().user(user).build();
        Payment payment = Payment.builder().id(paymentId).order(order).build();

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
        when(userService.getCurrentUser()).thenReturn(user);

        Payment result = paymentService.getById(paymentId);

        assertThat(result).isEqualTo(payment);
        verify(paymentRepository).findById(paymentId);
    }

    @Test
    void getAllByOrder_shouldReturnPaymentsIfUserMatches() {
        Long orderId = 2L;
        User user = new User();
        Order order = Order.builder().orderId(orderId).user(user).build();

        List<Payment> payments = List.of(new Payment(), new Payment());

        when(orderService.getById(orderId)).thenReturn(order);
        when(userService.getCurrentUser()).thenReturn(user);
        when(paymentRepository.findAllByOrder(order)).thenReturn(payments);

        List<Payment> result = paymentService.getAllByOrder(orderId);

        assertThat(result).hasSize(2);
        verify(paymentRepository).findAllByOrder(order);
    }
}

//package com.homegarden.store.backend.utils;
//
//import com.homegarden.store.backend.entity.Order;
//import com.homegarden.store.backend.enums.Status;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class OrderStatusChangerTest {
//
//    @Test
//    void getNextTestWhenCreated() {
//        Order order = Order.builder().orderId(1L).status(Status.CREATED).build();
//
//        Status result = OrderStatusChanger.getNext(order);
//
//        assertEquals(Status.CANCELLED, result);
//    }
//
//    @Test
//    void getNextTestWhenAwaitingPayment() {
//        Order order = Order.builder().orderId(1L).status(Status.AWAITING_PAYMENT).build();
//
//        Status result = OrderStatusChanger.getNext(order);
//
//        assertEquals(Status.CANCELLED, result);
//    }
//
//    @Test
//    void getNextTestWhenPaid() {
//        Order order = Order.builder().orderId(1L).status(Status.PAID).build();
//
//        Status result = OrderStatusChanger.getNext(order);
//
//        assertEquals(Status.SHIPPED, result);
//    }
//
//    @Test
//    void getNextTestWhenShipped() {
//        Order order = Order.builder().orderId(1L).status(Status.SHIPPED).build();
//
//        Status result = OrderStatusChanger.getNext(order);
//
//        assertEquals(Status.DELIVERED, result);
//    }
//
//    @Test
//    void getNextTestWhenCancelled() {
//        Order order = Order.builder().orderId(1L).status(Status.CANCELLED).build();
//        Status result = OrderStatusChanger.getNext(order);
//        assertEquals(Status.CANCELLED, result);
//    }
//}
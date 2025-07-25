package com.homegarden.store.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homegarden.store.backend.converter.Converter;
import com.homegarden.store.backend.dto.CreateOrderRequestDTO;
import com.homegarden.store.backend.dto.OrderResponseDTO;
import com.homegarden.store.backend.entity.Order;
import com.homegarden.store.backend.enums.Status;
import com.homegarden.store.backend.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)

class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private Converter<Order, CreateOrderRequestDTO, OrderResponseDTO> converter;

    private Order order;
    private OrderResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        order = Order.builder().orderId(1L).status(Status.CREATED).build();
        responseDTO = OrderResponseDTO.builder()
                .orderId(1L)
                .status(Status.CREATED)
                .build();
    }

    @Test
    void testGetAllOrders() throws Exception {
        when(orderService.getAll()).thenReturn(List.of(order));
        when(converter.toDto(order)).thenReturn(responseDTO);

        mockMvc.perform(get("/v1/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].orderId", is(1)));
    }

    @Test
    void testGetOrderById() throws Exception {
        when(orderService.getById(1L)).thenReturn(order);
        when(converter.toDto(order)).thenReturn(responseDTO);

        mockMvc.perform(get("/v1/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId", is(1)));
    }

    @Test
    void testGetOrdersByUser() throws Exception {
        when(orderService.getAllOrdersByUserId(1L)).thenReturn(List.of(order));
        when(converter.toDto(order)).thenReturn(responseDTO);

        mockMvc.perform(get("/v1/orders/history/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].orderId", is(1)));
    }

    @Test
    void testCancelOrder() throws Exception {
        mockMvc.perform(patch("/v1/orders/1/cancel"))
                .andExpect(status().isOk());
    }
}


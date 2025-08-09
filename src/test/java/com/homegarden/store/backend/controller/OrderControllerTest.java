package com.homegarden.store.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homegarden.store.backend.converter.OrderConverter;
import com.homegarden.store.backend.dto.CreateOrderItemRequestDto;
import com.homegarden.store.backend.dto.CreateOrderRequestDto;
import com.homegarden.store.backend.dto.OrderResponseDto;
import com.homegarden.store.backend.dto.TopCancelledProductDto;
import com.homegarden.store.backend.entity.Order;
import com.homegarden.store.backend.enums.Status;
import com.homegarden.store.backend.service.OrderService;
import com.homegarden.store.backend.service.security.JwtFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private OrderConverter converter;

    @MockitoBean
    private JwtFilter jwtFilter;

    private Order order;
    private OrderResponseDto responseDTO;

    @BeforeEach
    void setUp() {
        order = Order.builder()
                .orderId(1L)
                .status(Status.CREATED)
                .build();

        responseDTO = OrderResponseDto.builder()
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
        when(orderService.getAllByUser()).thenReturn(List.of(order));
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

    @Test
    void testCreateOrder() throws Exception {
        CreateOrderItemRequestDto item = new CreateOrderItemRequestDto(1L, 2);

        CreateOrderRequestDto requestDTO = new CreateOrderRequestDto(
                List.of(item),
                "Test Address",
                "Courier");


        when(orderService.create(any(CreateOrderRequestDto.class))).thenReturn(order);
        when(converter.toDto(order)).thenReturn(responseDTO);

        mockMvc.perform(post("/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId", is(1)));
    }

    @Test
    void testGetTopCancelledProducts() throws Exception {
        TopCancelledProductDto dto = new TopCancelledProductDto(10L, "Product Name", 5L);
        when(orderService.getTopCancelledProducts()).thenReturn(List.of(dto));

        mockMvc.perform(get("/v1/orders/report/top-cancelled-products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].productId", is(10)))
                .andExpect(jsonPath("$[0].productName", is("Product Name")))
                .andExpect(jsonPath("$[0].cancelCount", is(5)));
    }
}
package com.homegarden.store.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homegarden.store.backend.converter.CartConverter;
import com.homegarden.store.backend.dto.CartResponseDTO;
import com.homegarden.store.backend.dto.CreateCartRequestDTO;
import com.homegarden.store.backend.entity.Cart;
import com.homegarden.store.backend.entity.User;
import com.homegarden.store.backend.exception.CartAlreadyExistsException;
import com.homegarden.store.backend.exception.CartNotFoundException;
import com.homegarden.store.backend.exception.UserNotFoundException;
import com.homegarden.store.backend.service.CartService;
import com.homegarden.store.backend.service.security.JwtFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
@AutoConfigureMockMvc(addFilters = false)
class CartControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    CartService cartServiceTest;

    @MockitoBean
    CartConverter cartConverterTest;

    @MockitoBean
    private JwtFilter jwtFilter;

    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();

    CreateCartRequestDTO createCartRequestDTO = new CreateCartRequestDTO(1L);

    User user = User.builder().userId(1L).build();
    Cart cartForCreate = Cart.builder().user(user).build();

    Cart cart = new Cart(1L, new ArrayList<>(), user);
    CartResponseDTO cartResponseDTO = new CartResponseDTO(1L, 1L);

    @Test
    void createTestWhenCartNotExist() throws Exception {
        when(cartConverterTest.toEntity(createCartRequestDTO)).thenReturn(cartForCreate);
        when(cartServiceTest.create(cartForCreate)).thenReturn(cart);
        when(cartConverterTest.toDto(cart)).thenReturn(cartResponseDTO);

        mockMvc.perform(post("/v1/carts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCartRequestDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cartId").value(1L))
                .andExpect(jsonPath("$.userId").value(1L));
    }

    @Test
    void createTestWhenUserNotExist() throws Exception {
        when(cartConverterTest.toEntity(createCartRequestDTO)).thenReturn(cartForCreate);
        doThrow(new UserNotFoundException("User not found")).when(cartServiceTest).create(cartForCreate);

        mockMvc.perform(post("/v1/carts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCartRequestDTO)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void createTestWhenCartExists() throws Exception {
        when(cartConverterTest.toEntity(createCartRequestDTO)).thenReturn(cartForCreate);
        doThrow(new CartAlreadyExistsException("Cart already exists for this user"))
                .when(cartServiceTest).create(cartForCreate);

        mockMvc.perform(post("/v1/carts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCartRequestDTO)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    void getByIdSuccessful() throws Exception {
        when(cartServiceTest.getById(1L)).thenReturn(cart);
        when(cartConverterTest.toDto(cart)).thenReturn(cartResponseDTO);

        mockMvc.perform(get("/v1/carts/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartId").value(1L));
    }

    @Test
    void getByIdWhenCartNotFound() throws Exception {
        doThrow(new CartNotFoundException("Cart not found")).when(cartServiceTest).getById(999L);

        mockMvc.perform(get("/v1/carts/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getAll() throws Exception {
        when(cartServiceTest.getAll()).thenReturn(List.of(cart));
        when(cartConverterTest.toDto(cart)).thenReturn(cartResponseDTO);

        mockMvc.perform(get("/v1/carts"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cartId").value(1L));
    }

    @Test
    void deleteTest() throws Exception {
        doNothing().when(cartServiceTest).delete(1L);

        mockMvc.perform(delete("/v1/carts/1"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
package com.homegarden.store.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homegarden.store.backend.converter.CartItemConverter;
import com.homegarden.store.backend.dto.CartItemResponseDTO;
import com.homegarden.store.backend.dto.CreateCartItemRequestDTO;
import com.homegarden.store.backend.dto.UpdateCartItemRequestDTO;
import com.homegarden.store.backend.entity.Cart;
import com.homegarden.store.backend.entity.CartItem;
import com.homegarden.store.backend.entity.Product;
import com.homegarden.store.backend.entity.User;
import com.homegarden.store.backend.exception.CartItemNotFoundException;
import com.homegarden.store.backend.exception.CartNotFoundException;
import com.homegarden.store.backend.exception.ProductNotFoundException;
import com.homegarden.store.backend.service.CartItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartItemController.class)
@AutoConfigureMockMvc(addFilters = false)
class CartItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CartItemService cartItemServiceTest;

    @MockitoBean
    private CartItemConverter cartItemConverter;

    @Autowired
    private ObjectMapper objectMapper;

    CreateCartItemRequestDTO dto = new CreateCartItemRequestDTO(1L, 1L, 10);

    CartItem cartItemConverted = CartItem.builder()
            .cart(Cart.builder().cartId(1L).build())
            .product(Product.builder().productId(1L).name("TestProduct").build())
            .quantity(10)
            .build();

    CartItemResponseDTO responseDTO = new CartItemResponseDTO(1L, 1L, 1L, "TestProduct", 10);

    UpdateCartItemRequestDTO updateDto = new UpdateCartItemRequestDTO(20);
    CartItemResponseDTO updatedResponseDto = new CartItemResponseDTO(1L, 1L, 1L, "TestProduct", 20);

    Cart cart = new Cart(1L, new ArrayList<>(), User.builder().userId(1L).build());
    Product product = Product.builder().productId(1L).name("TestProduct").build();
    CartItem cartItem = new CartItem(1L, cart, product, 10);
    CartItem updatedCartItem = new CartItem(1L, cart, product, 20);

    @Test
    void updateQuantityTest() throws Exception {
        when(cartItemServiceTest.getById(1L)).thenReturn(cartItem);
        when(cartItemServiceTest.updateQuantity(1L, updateDto.quantity()))
                .thenReturn(Optional.ofNullable(updatedCartItem));
        when(cartItemConverter.toDto(updatedCartItem)).thenReturn(updatedResponseDto);

        mockMvc.perform(put("/v1/cart-items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartId").value(1))
                .andExpect(jsonPath("$.productId").value(1))
                .andExpect(jsonPath("$.quantity").value(updatedResponseDto.quantity()));
    }

    @Test
    void createTestSuccessful() throws Exception {
        when(cartItemConverter.toEntity(dto)).thenReturn(cartItemConverted);
        when(cartItemServiceTest.create(cartItemConverted)).thenReturn(cartItem);
        when(cartItemConverter.toDto(cartItem)).thenReturn(responseDTO);

        mockMvc.perform(post("/v1/cart-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.productId").value(1))
                .andExpect(jsonPath("$.cartId").value(1))
                .andExpect(jsonPath("$.quantity").value(responseDTO.quantity()));
    }

    @Test
    void createTestWhenCartNotFound() throws Exception {
        when(cartItemConverter.toEntity(dto)).thenReturn(cartItemConverted);
        doThrow(new CartNotFoundException("Cart not found"))
                .when(cartItemServiceTest).create(cartItemConverted);

        mockMvc.perform(post("/v1/cart-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void createTestWhenProductNotFound() throws Exception {
        when(cartItemConverter.toEntity(dto)).thenReturn(cartItemConverted);
        doThrow(new ProductNotFoundException("Product not found"))
                .when(cartItemServiceTest).create(cartItemConverted);

        mockMvc.perform(post("/v1/cart-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getByIdTestSuccessful() throws Exception {
        when(cartItemServiceTest.getById(1L)).thenReturn(cartItem);
        when(cartItemConverter.toDto(cartItem)).thenReturn(responseDTO);

        mockMvc.perform(get("/v1/cart-items/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.productId").value(1))
                .andExpect(jsonPath("$.cartId").value(1))
                .andExpect(jsonPath("$.quantity").value(responseDTO.quantity()));
    }

    @Test
    void getByIdTestWhenCartItemNotFound() throws Exception {
        doThrow(new CartItemNotFoundException("Cart item not found"))
                .when(cartItemServiceTest).getById(1L);

        mockMvc.perform(get("/v1/cart-items/1"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllTest() throws Exception {
        when(cartItemServiceTest.getAll()).thenReturn(List.of(cartItem));
        when(cartItemConverter.toDto(cartItem)).thenReturn(responseDTO);

        mockMvc.perform(get("/v1/cart-items"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].productId").value(1))
                .andExpect(jsonPath("$[0].cartId").value(1))
                .andExpect(jsonPath("$[0].quantity").value(10));
    }

    @Test
    void deleteTest() throws Exception {
        doNothing().when(cartItemServiceTest).delete(1L);

        mockMvc.perform(delete("/v1/cart-items/1"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
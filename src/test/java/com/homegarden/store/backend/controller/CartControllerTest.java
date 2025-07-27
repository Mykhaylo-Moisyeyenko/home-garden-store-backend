package com.homegarden.store.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homegarden.store.backend.converter.CartConverter;
import com.homegarden.store.backend.dto.CartResponseDTO;
import com.homegarden.store.backend.dto.CreateCartRequestDTO;
import com.homegarden.store.backend.entity.Cart;
import com.homegarden.store.backend.entity.User;
import com.homegarden.store.backend.service.CartService;
import com.homegarden.store.backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    UserService userServiceTest;

    @MockitoBean
    CartConverter cartConverterTest;

    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();

    CreateCartRequestDTO createCartRequestDTO = new CreateCartRequestDTO(1L);
    User user = User.builder().userId(1L).build();
    Cart cart = new Cart(1L, new ArrayList<>(), user);
    CartResponseDTO cartResponseDTO = new CartResponseDTO(1L, 1L);

    //
//    @PostMapping
//    public ResponseEntity<?> create(@RequestBody @Valid CreateCartRequestDTO dto) {
//        User user = userService.getById(dto.userId());
//        if(cartService.existsByUserId(dto.userId())) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).body("Cart already exists for this user");
//        }
//            Cart cart = cartConverter.toEntity(dto);
//            cart.setUser(user);
//            Cart created = cartService.create(cart);
//            return ResponseEntity.status(HttpStatus.CREATED).body(cartConverter.toDto(created));
//    }

    @Test
    void createTestWhenCartNotExist() throws Exception {
        when(userServiceTest.getById(1L)).thenReturn(user);
        when(cartServiceTest.existsByUserId(1L)).thenReturn(false);
        when(cartConverterTest.toEntity(createCartRequestDTO)).thenReturn(cart);
        cart.setUser(user);
        when(cartServiceTest.create(cart)).thenReturn(cart);
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
    void getById() {
    }

    @Test
    void getAll() {
    }

    @Test
    void delete() {
    }
}
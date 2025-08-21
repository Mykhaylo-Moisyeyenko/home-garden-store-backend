package com.homegarden.store.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homegarden.store.backend.converter.CartConverter;
import com.homegarden.store.backend.converter.CartItemConverter;
import com.homegarden.store.backend.dto.CartItemResponseDto;
import com.homegarden.store.backend.dto.CartResponseDto;
import com.homegarden.store.backend.dto.CreateCartItemRequestDto;
import com.homegarden.store.backend.dto.UpdateCartItemRequestDto;
import com.homegarden.store.backend.entity.Cart;
import com.homegarden.store.backend.entity.CartItem;
import com.homegarden.store.backend.entity.Product;
import com.homegarden.store.backend.entity.User;
import com.homegarden.store.backend.service.CartService;
import com.homegarden.store.backend.service.security.JwtFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
@AutoConfigureMockMvc(addFilters = false)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CartService cartServiceTest;

    @MockitoBean
    private CartConverter cartConverterTest;

    @MockitoBean
    private JwtFilter jwtFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CartItemConverter cartItemConverter;

    CartItem cartItem1 = CartItem.builder()
            .cartItemId(1L)
            .product(Product.builder()
                    .productId(1L)
                    .name("Pillow")
                    .build())
            .quantity(10)
            .build();

    CartItem cartItem2 = CartItem.builder()
            .product(Product.builder()
                    .productId(1L)
                    .build())
            .quantity(10)
            .build();

    CartItem cartItemUpdated = CartItem.builder()
            .cartItemId(1L)
            .product(Product.builder()
                    .productId(1L)
                    .build())
            .quantity(100)
            .build();

    CartItemResponseDto dto = new CartItemResponseDto(1L,
            "Pillow",
            10);

    CartItemResponseDto dtoUpdated = new CartItemResponseDto(1L,
            "Pillow",
            100);

    CreateCartItemRequestDto createDto = new CreateCartItemRequestDto(1L, 10);
    User user = User.builder().userId(1L).build();
    Cart cart1 = new Cart(1L, List.of(cartItem1), user);
    Cart cartUpdated = new Cart(1L, List.of(cartItemUpdated), user);
    Cart cartEmpty = new Cart(1L, List.of(), user);
    CartResponseDto cartResponseDto = new CartResponseDto(1L, 1L, List.of(dto));
    CartResponseDto cartResponseDtoUpdated = new CartResponseDto(1L, 1L, List.of(dtoUpdated));
    CartResponseDto cartResponseDtoEmpty = new CartResponseDto(1L, 1L, List.of());
    UpdateCartItemRequestDto updateDto = new UpdateCartItemRequestDto(1L, 100);

    @Test
    void getAllCartItemsTest() throws Exception {

        when(cartServiceTest.getAllCartItems()).thenReturn(List.of(cartItem1));
        when(cartItemConverter.toDto(cartItem1)).thenReturn(dto);

        mockMvc.perform(get("/v1/carts"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(1L))
                .andExpect(jsonPath("$[0].productName").value("Pillow"))
                .andExpect(jsonPath("$[0].quantity").value(10));
    }

    @Test
    void deleteTest() throws Exception {
        doNothing().when(cartServiceTest).delete();

        mockMvc.perform(delete("/v1/carts"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void addCartItemTest() throws Exception {
        when(cartItemConverter.toEntity(any(CreateCartItemRequestDto.class))).thenReturn(cartItem2);
        when(cartServiceTest.addCartItem(any(CartItem.class))).thenReturn(cart1);
        when(cartConverterTest.toDto(any(Cart.class))).thenReturn(cartResponseDto);

        mockMvc.perform(post("/v1/carts/item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cartId").value(1L))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.items[0].productName").value("Pillow"));
    }

    @Test
    void updateCartItemQuantityTest() throws Exception {
        when(cartServiceTest.updateCartItemQuantity(updateDto.cartItemId(), updateDto.quantity()))
                .thenReturn(cartUpdated);
        when(cartConverterTest.toDto(any(Cart.class))).thenReturn(cartResponseDtoUpdated);

        mockMvc.perform(put("/v1/carts/item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartId").value(1L))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.items[0].quantity").value(100));
    }

    @Test
    void deleteCartItemTest() throws Exception {
        when(cartServiceTest.deleteCartItem(1L)).thenReturn(cartEmpty);
        when(cartConverterTest.toDto(any(Cart.class))).thenReturn(cartResponseDtoEmpty);

        mockMvc.perform(delete("/v1/carts/item/1"))
                .andDo(print())
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.cartId").value(1L))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.items", hasSize(0)));
    }
}
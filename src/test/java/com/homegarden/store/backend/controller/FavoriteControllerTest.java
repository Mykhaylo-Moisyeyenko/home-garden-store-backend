package com.homegarden.store.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homegarden.store.backend.dto.FavoriteDto;
import com.homegarden.store.backend.entity.Favorite;
import com.homegarden.store.backend.entity.User;
import com.homegarden.store.backend.service.FavoriteService;
import com.homegarden.store.backend.service.ProductService;
import com.homegarden.store.backend.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FavoriteController.class)
@AutoConfigureMockMvc(addFilters = false)
class FavoriteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FavoriteService favoriteService;

    @MockitoBean
    private UserService userService;

    @Test
    @DisplayName("GET /v1/favorites/{userId} should return favorite list")
    void shouldReturnFavoriteList() throws Exception {
        FavoriteDto favoriteDto = new FavoriteDto(1L, 200L);

        Favorite favorite = Favorite.builder()
                .favoriteId(1L)
                .user(null)
                .product(null)
                .build();

        User user = User.builder()
                .userId(1L)
                .email("test@example.com")
                .passwordHash("hashedPassword")
                .favorites(favorite)
                .build();

        when(userService.existsById(1L)).thenReturn(true);
        when(userService.getById(1L)).thenReturn(user);
        when(favoriteService.getAll(1L)).thenReturn(List.of(favoriteDto)); // ← это критично

        mockMvc.perform(get("/v1/favorites/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(1L))
                .andExpect(jsonPath("$[0].productId").value(200L));
    }

    @Test
    @DisplayName("POST /v1/favorites should add product to favorites")
    void shouldAddToFavorites() throws Exception {
        FavoriteDto dto = new FavoriteDto(1L, 200L);

        mockMvc.perform(post("/v1/favorites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("DELETE /v1/favorites should remove product from favorites")
    void shouldRemoveFromFavorites() throws Exception {
        FavoriteDto dto = new FavoriteDto(1L, 200L);

        mockMvc.perform(delete("/v1/favorites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNoContent());
    }
}
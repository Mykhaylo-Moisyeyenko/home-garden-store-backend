package com.homegarden.store.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homegarden.store.backend.config.SecurityConfig;
import com.homegarden.store.backend.controller.FavoriteController;
import com.homegarden.store.backend.model.dto.FavoriteDto;
import com.homegarden.store.backend.service.FavoriteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FavoriteController.class)
@AutoConfigureMockMvc(addFilters = false)
class FavoriteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FavoriteService favoriteService;

    @Test
    @DisplayName("GET /v1/favorites/{userId} должен возвращать список избранного")
    void shouldReturnFavoriteList() throws Exception {
        FavoriteDto favoriteDto = new FavoriteDto(1L, 200L);
        when(favoriteService.getAll(1L)).thenReturn(List.of(favoriteDto));

        mockMvc.perform(get("/v1/favorites/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(1L))
                .andExpect(jsonPath("$[0].productId").value(200L));
    }

    @Test
    @DisplayName("POST /v1/favorites должен добавлять товар в избранное")
    void shouldAddToFavorites() throws Exception {
        FavoriteDto dto = new FavoriteDto(1L, 200L);

        mockMvc.perform(post("/v1/favorites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("DELETE /v1/favorites должен удалять товар из избранного")
    void shouldRemoveFromFavorites() throws Exception {
        FavoriteDto dto = new FavoriteDto(1L, 200L);

        mockMvc.perform(delete("/v1/favorites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNoContent());
    }
}

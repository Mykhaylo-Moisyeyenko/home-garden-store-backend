package com.homegarden.store.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homegarden.store.backend.converter.Converter;
import com.homegarden.store.backend.dto.FavoriteDto;
import com.homegarden.store.backend.entity.Favorite;
import com.homegarden.store.backend.entity.Product;
import com.homegarden.store.backend.entity.User;
import com.homegarden.store.backend.service.FavoriteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FavoriteController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(FavoriteControllerTest.TestConfig.class)
class FavoriteControllerTest {

    private final MockMvc mockMvc;
    private final FavoriteService favoriteService;
    private final Converter<Favorite, FavoriteDto, FavoriteDto> converter;
    private final ObjectMapper objectMapper;

    @Autowired
    public FavoriteControllerTest(MockMvc mockMvc,
                                  FavoriteService favoriteService,
                                  Converter<Favorite, FavoriteDto, FavoriteDto> converter,
                                  ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.favoriteService = favoriteService;
        this.converter = converter;
        this.objectMapper = objectMapper;
    }

    private final Long userId = 1L;
    private final Long productId = 100L;

    private final Favorite favorite = Favorite.builder()
            .user(User.builder().userId(userId).build())
            .product(Product.builder().productId(productId).build())
            .build();

    private final FavoriteDto favoriteDto = new FavoriteDto(userId, productId);

    @Test
    @DisplayName("GET /v1/favorites/{userId} should return list of favoriteDto")
    void getAllFavorites_shouldReturnList() throws Exception {
        when(favoriteService.getAll(userId)).thenReturn(List.of(favorite));
        when(converter.toDto(favorite)).thenReturn(favoriteDto);

        mockMvc.perform(get("/v1/favorites/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(userId))
                .andExpect(jsonPath("$[0].productId").value(productId));

        verify(favoriteService).getAll(userId);
    }

    @Test
    @DisplayName("POST /v1/favorites should add favorite")
    void addFavorite_shouldCallService() throws Exception {
        when(converter.toEntity(favoriteDto)).thenReturn(favorite);

        mockMvc.perform(post("/v1/favorites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(favoriteDto)))
                .andExpect(status().isCreated());

        verify(converter).toEntity(favoriteDto);
        verify(favoriteService).addToFavorites(favorite);
    }

    @Test
    @DisplayName("DELETE /v1/favorites should remove favorite")
    void removeFavorite_shouldCallService() throws Exception {
        when(converter.toEntity(favoriteDto)).thenReturn(favorite);

        mockMvc.perform(delete("/v1/favorites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(favoriteDto)))
                .andExpect(status().isNoContent());

        verify(converter, atLeastOnce()).toEntity(favoriteDto);
        verify(favoriteService).removeFromFavorites(favorite);
    }

    @Test
    @DisplayName("POST /v1/favorites with invalid body should return 400")
    void addFavorite_withInvalidPayload_shouldReturnBadRequest() throws Exception {
        FavoriteDto invalidDto = new FavoriteDto(null, null);

        mockMvc.perform(post("/v1/favorites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public FavoriteService favoriteService() {
            return mock(FavoriteService.class);
        }

        @Bean
        public Converter<Favorite, FavoriteDto, FavoriteDto> converter() {
            return mock(Converter.class);
        }
    }
}



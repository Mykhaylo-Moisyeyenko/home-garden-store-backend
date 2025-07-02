package com.homegarden.store.backend.service;

import com.homegarden.store.backend.converter.FavoriteConverter;
import com.homegarden.store.backend.model.dto.FavoriteDto;
import com.homegarden.store.backend.model.entity.Favorite;
import com.homegarden.store.backend.repository.FavoriteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class FavoriteServiceImplTest {

    private FavoriteRepository favoriteRepository;
    private FavoriteServiceImpl favoriteService;

    @BeforeEach
    void setUp() {
        favoriteRepository = mock(FavoriteRepository.class);
        favoriteService = new FavoriteServiceImpl(favoriteRepository);
    }

    @Test
    void getAll_shouldReturnListOfDto() {
        Favorite entity = Favorite.builder().userId(1L).productId(101L).build();
        when(favoriteRepository.findByUserId(1L)).thenReturn(List.of(entity));

        List<FavoriteDto> result = favoriteService.getAll(1L);

        assertEquals(1, result.size());
        assertEquals(101L, result.get(0).productId());
        verify(favoriteRepository).findByUserId(1L);
    }

    @Test
    void addToFavorites_shouldSaveIfNotExists() {
        FavoriteDto dto = new FavoriteDto(1L, 101L);
        when(favoriteRepository.findByUserIdAndProductId(1L, 101L)).thenReturn(Optional.empty());

        favoriteService.addToFavorites(dto);

        verify(favoriteRepository).save(any(Favorite.class));
    }

    @Test
    void addToFavorites_shouldNotSaveIfExists() {
        FavoriteDto dto = new FavoriteDto(1L, 101L);
        when(favoriteRepository.findByUserIdAndProductId(1L, 101L))
                .thenReturn(Optional.of(new Favorite()));

        favoriteService.addToFavorites(dto);

        verify(favoriteRepository, never()).save(any());
    }

    @Test
    void removeFromFavorites_shouldCallDelete() {
        FavoriteDto dto = new FavoriteDto(1L, 101L);

        favoriteService.removeFromFavorites(dto);

        verify(favoriteRepository).deleteByUserIdAndProductId(1L, 101L);
    }
}
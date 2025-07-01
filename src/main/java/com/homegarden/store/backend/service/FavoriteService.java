package com.homegarden.store.backend.service;

import com.homegarden.store.backend.model.dto.FavoriteDto;
import jakarta.validation.Valid;

import java.util.List;

public interface FavoriteService {

    List<FavoriteDto> getAll(Long userId);

    void addToFavorites(FavoriteDto favoriteDto);

    void removeFromFavorites(@Valid FavoriteDto favoriteDto);
}

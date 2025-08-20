package com.homegarden.store.backend.controller;

import com.homegarden.store.backend.controller.api.FavoriteControllerApi;
import com.homegarden.store.backend.converter.FavoriteConverter;
import com.homegarden.store.backend.dto.FavoriteDto;
import com.homegarden.store.backend.entity.Favorite;
import com.homegarden.store.backend.service.FavoriteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class FavoriteController implements FavoriteControllerApi {

    private final FavoriteService favoriteService;
    private final FavoriteConverter favoriteConverter;

    @Override
    @PreAuthorize("hasAnyRole('USER','ADMINISTRATOR')")
    public ResponseEntity<List<FavoriteDto>> getAll() {
        List<FavoriteDto> favorites = favoriteService.getAll().stream()
                .map(favoriteConverter::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(favorites);
    }

    @Override
    @PreAuthorize("hasAnyRole('USER','ADMINISTRATOR')")
    public ResponseEntity<Void> addToFavorites(@Valid FavoriteDto favoriteDto) {
        Favorite favorite = favoriteConverter.toEntity(favoriteDto);
        favoriteService.addToFavorites(favorite);

        return ResponseEntity.status(201).build();
    }

    @Override
    @PreAuthorize("hasAnyRole('USER','ADMINISTRATOR')")
    public ResponseEntity<Void> removeFromFavorites(@Valid FavoriteDto favoriteDto) {
        Favorite favorite = favoriteConverter.toEntity(favoriteDto);
        favoriteService.removeFromFavorites(favorite);

        return ResponseEntity.noContent().build();
    }
}

package com.homegarden.store.backend.controller;

import com.homegarden.store.backend.converter.Converter;
import com.homegarden.store.backend.dto.FavoriteDto;
import com.homegarden.store.backend.entity.Favorite;
import com.homegarden.store.backend.service.FavoriteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final Converter<Favorite, FavoriteDto, FavoriteDto> converter;

    @GetMapping("/{userId}")
    public ResponseEntity<List<FavoriteDto>> getAll(@PathVariable @Min(1) Long userId) {
        List<FavoriteDto> response = favoriteService.getAll(userId)
                .stream()
                .map(converter::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Void> addToFavorites(@Valid @RequestBody FavoriteDto favoriteDto) {
        favoriteService.addToFavorites(converter.toEntity(favoriteDto));

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> removeFromFavorites(@RequestBody @Valid FavoriteDto favoriteDto) {
        favoriteService.removeFromFavorites(converter.toEntity(favoriteDto));

        return ResponseEntity.noContent().build();
    }
}
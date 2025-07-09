package com.homegarden.store.backend.controller;

import com.homegarden.store.backend.converter.Converter;
import com.homegarden.store.backend.dto.FavoriteDto;
import com.homegarden.store.backend.entity.Favorite;
import com.homegarden.store.backend.service.FavoriteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/favorites")
@RequiredArgsConstructor
@Validated
public class FavoriteController {

    private final FavoriteService favoriteService;

    private final Converter<Favorite, FavoriteDto, FavoriteDto> converter;

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<FavoriteDto> getAll(@PathVariable @Min(1) Long userId) {
        return favoriteService.getAll(userId)
                .stream()
                .map(converter::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addToFavorites(@RequestBody @Valid FavoriteDto favoriteDto) {
        Favorite entity = converter.toEntity(favoriteDto);
        favoriteService.addToFavorites(entity);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFromFavorites(@RequestBody @Valid FavoriteDto favoriteDto) {
        Favorite entity = converter.toEntity(favoriteDto);
        favoriteService.removeFromFavorites(entity);
    }
}
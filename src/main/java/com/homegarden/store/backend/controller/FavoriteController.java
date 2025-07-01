package com.homegarden.store.backend.controller;

import com.homegarden.store.backend.model.dto.FavoriteDto;
import com.homegarden.store.backend.service.FavoriteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/favorites")
@RequiredArgsConstructor
@Validated
public class FavoriteController {

    private final FavoriteService favoriteService;

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<FavoriteDto> getAll(@PathVariable @Min(1) Long userId) {
        return favoriteService.getAll(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addToFavorites(@RequestBody @Valid FavoriteDto favoriteDto){
        favoriteService.addToFavorites(favoriteDto);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFromFavorites(@RequestBody @Valid FavoriteDto favoriteDto){
        favoriteService.removeFromFavorites(favoriteDto);
    }
}

package com.homegarden.store.backend.controller;

import com.homegarden.store.backend.exception.UserNotFoundException;
import com.homegarden.store.backend.model.dto.FavoriteDto;
import com.homegarden.store.backend.service.FavoriteService;
import com.homegarden.store.backend.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/favorites")
@RequiredArgsConstructor
@Validated
public class FavoriteController {

    private final FavoriteService favoriteService;

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<FavoriteDto>> getAll(@PathVariable Long userId) {
        List<FavoriteDto> favorites = favoriteService.getAll(userId);
        return ResponseEntity.ok(favorites);
    }

    @PostMapping
    public ResponseEntity<Void> addToFavorites(@RequestBody @Valid FavoriteDto favoriteDto) {
        favoriteService.addToFavorites(favoriteDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> removeFromFavorites(@RequestBody @Valid FavoriteDto favoriteDto) {
        favoriteService.removeFromFavorites(favoriteDto);
        return ResponseEntity.noContent().build();
    }
}
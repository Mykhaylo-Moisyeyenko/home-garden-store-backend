package com.homegarden.store.backend.controller;

import com.homegarden.store.backend.dto.FavoriteDto;
import com.homegarden.store.backend.exception.UserNotFoundException;
import com.homegarden.store.backend.service.FavoriteService;
import com.homegarden.store.backend.service.UserService;
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

    private final UserService userService;

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<FavoriteDto> getAll(@PathVariable @Min(1) Long userId) {
        if (!userService.existsById(userId)) {
            throw new UserNotFoundException("User with id " + userId + " doesn't exists");
        }
        return favoriteService.getAll(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addToFavorites(@RequestBody @Valid FavoriteDto favoriteDto) {
        favoriteService.addToFavorites(favoriteDto);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFromFavorites(@RequestBody @Valid FavoriteDto favoriteDto) {
        favoriteService.removeFromFavorites(favoriteDto);
    }
}
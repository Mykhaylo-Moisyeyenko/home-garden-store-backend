package com.homegarden.store.backend.service;

import com.homegarden.store.backend.converter.FavoriteConverter;
import com.homegarden.store.backend.dto.FavoriteDto;
import com.homegarden.store.backend.entity.Favorite;
import com.homegarden.store.backend.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;

    private final ProductService productService;

    private final UserService userService;

    @Override
    public List<FavoriteDto> getAll(Long userId) {
        List<Favorite> favorites = favoriteRepository.findByUser_UserId(userId);
        List<FavoriteDto> favoriteDtos = favorites.stream()
                .map(FavoriteConverter::toDto)
                .collect(Collectors.toList());
        return favoriteDtos;
    }

    @Override
    public void addToFavorites(FavoriteDto favoriteDto) {
        userService.getById(favoriteDto.userId());
        productService.getById(favoriteDto.productId());

        Optional<Favorite> favorite = favoriteRepository
                .findByUser_UserIdAndProduct_ProductId(favoriteDto.userId(), favoriteDto.productId());

        if (favorite.isPresent()) {
            return;
        }
        favoriteRepository.save(FavoriteConverter.toEntity(favoriteDto));
    }

    @Override
    public void removeFromFavorites(FavoriteDto favoriteDto) {
        favoriteRepository.deleteByUser_UserIdAndProduct_ProductId(favoriteDto.userId(), favoriteDto.productId());

    }
}
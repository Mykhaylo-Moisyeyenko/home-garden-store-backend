package com.homegarden.store.backend.service;

import com.homegarden.store.backend.converter.FavoriteConverter;
import com.homegarden.store.backend.model.dto.FavoriteDto;
import com.homegarden.store.backend.model.entity.Favorite;
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

    @Override
    public List<FavoriteDto> getAll(Long userId) {
        List<Favorite> favorites = favoriteRepository.findByUserId(userId);
        List<FavoriteDto> favoriteDtos = favorites.stream()
                .map(FavoriteConverter::toDto)
                .collect(Collectors.toList());
        return favoriteDtos;
    }

    @Override
    public void addToFavorites(FavoriteDto favoriteDto) {
        Optional<Favorite> favorite = favoriteRepository.findByUserIdAndProductId(favoriteDto.userId(), favoriteDto.productId());
        if (favorite.isPresent()) {
            return;
        }
        favoriteRepository.save(FavoriteConverter.toEntity(favoriteDto));
    }

    @Override
    public void removeFromFavorites(FavoriteDto favoriteDto) {
        favoriteRepository.deleteByUserIdAndProductId(favoriteDto.userId(), favoriteDto.productId());
    }
}

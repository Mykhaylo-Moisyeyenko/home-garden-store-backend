package com.homegarden.store.backend.converter;

import com.homegarden.store.backend.model.dto.FavoriteDto;
import com.homegarden.store.backend.model.entity.Favorite;

public class FavoriteConverter {

    public static Favorite toEntity(FavoriteDto favoriteDto) {
        return Favorite.builder()
                .userId(favoriteDto.userId())
                .productId(favoriteDto.productId())
                .build();
    }

    public static FavoriteDto toDto(Favorite favorite) {
        return new FavoriteDto(favorite.getUserId(), favorite.getProductId());
    }
}
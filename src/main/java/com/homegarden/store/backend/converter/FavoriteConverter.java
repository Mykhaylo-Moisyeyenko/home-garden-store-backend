package com.homegarden.store.backend.converter;

import com.homegarden.store.backend.model.dto.FavoriteDto;
import com.homegarden.store.backend.model.entity.Favorite;
import com.homegarden.store.backend.model.entity.Product;
import com.homegarden.store.backend.model.entity.User;

public class FavoriteConverter {

    public static Favorite toEntity(FavoriteDto favoriteDto) {
        return Favorite.builder()
                .user(User.builder().userId(favoriteDto.userId()).build())
                .product(Product.builder().productId(favoriteDto.productId()).build())
                .build();
    }

    public static FavoriteDto toDto(Favorite favorite) {
        return FavoriteDto.builder()
                .userId(favorite.getUser().getUserId())
                .productId(favorite.getProduct().getProductId())
                .build();
    }
}

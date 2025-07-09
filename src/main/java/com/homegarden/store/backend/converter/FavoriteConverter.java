package com.homegarden.store.backend.converter;

import com.homegarden.store.backend.dto.FavoriteDto;
import com.homegarden.store.backend.entity.Favorite;
import com.homegarden.store.backend.entity.Product;
import com.homegarden.store.backend.entity.User;

public class FavoriteConverter {

    public static Favorite toEntity(FavoriteDto favoriteDto) {
        return Favorite.builder()
                .user(User.builder().userId(favoriteDto.userId()).build())
                .product(Product.builder().productId(favoriteDto.productId()).build())
                .build();
    }

    public static FavoriteDto toDto(Favorite favorite) {
        return new FavoriteDto(favorite.getUser().getUserId(), favorite.getProduct().getProductId());
    }
}
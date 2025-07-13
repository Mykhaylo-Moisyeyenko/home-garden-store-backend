package com.homegarden.store.backend.converter;

import com.homegarden.store.backend.dto.FavoriteDto;
import com.homegarden.store.backend.entity.Favorite;
import com.homegarden.store.backend.entity.Product;
import com.homegarden.store.backend.entity.User;
import org.springframework.stereotype.Component;

@Component
public class FavoriteConverter implements Converter<Favorite, FavoriteDto, FavoriteDto> {

    public Favorite toEntity(FavoriteDto favoriteDto) {
        return Favorite.builder()
                .user(User.builder().userId(favoriteDto.userId()).build())
                .product(Product.builder().productId(favoriteDto.productId()).build())
                .build();
    }

    public FavoriteDto toDto(Favorite favorite) {
        return new FavoriteDto(favorite.getUser().getUserId(), favorite.getProduct().getProductId());
    }
}
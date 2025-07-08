package com.homegarden.store.backend.converter;

import com.homegarden.store.backend.model.dto.FavoriteDto;
import com.homegarden.store.backend.model.entity.Favorite;
import com.homegarden.store.backend.model.entity.Product;
import com.homegarden.store.backend.model.entity.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FavoriteConverterTest {

    @Test
    void toEntity_shouldConvertCorrectly() {
        FavoriteDto dto = new FavoriteDto(1L, 200L);

        Favorite entity = FavoriteConverter.toEntity(dto);

        assertThat(entity.getUser().getUserId()).isEqualTo(1L);
        assertThat(entity.getProduct().getProductId()).isEqualTo(200L);

    }

    @Test
    void toDto_shouldConvertCorrectly() {
        Favorite entity = Favorite.builder()
                .user(User.builder().userId(1L).build())
                .product(Product.builder().productId(200L).build())
                .build();

        FavoriteDto dto = FavoriteConverter.toDto(entity);

        assertThat(dto.userId()).isEqualTo(1L);
        assertThat(dto.productId()).isEqualTo(200L);
    }
}

package com.homegarden.store.backend.converter;

import com.homegarden.store.backend.dto.FavoriteDto;
import com.homegarden.store.backend.entity.Favorite;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FavoriteConverterTest {

    @Test
    void toEntity_shouldConvertCorrectly() {
        FavoriteDto dto = new FavoriteDto(1L, 200L);

        Favorite entity = FavoriteConverter.toEntity(dto);

        assertThat(entity.getUserId()).isEqualTo(1L);
        assertThat(entity.getProductId()).isEqualTo(200L);
    }

    @Test
    void toDto_shouldConvertCorrectly() {
        Favorite entity = Favorite.builder()
                .userId(1L)
                .productId(200L)
                .build();

        FavoriteDto dto = FavoriteConverter.toDto(entity);

        assertThat(dto.userId()).isEqualTo(1L);
        assertThat(dto.productId()).isEqualTo(200L);
    }
}
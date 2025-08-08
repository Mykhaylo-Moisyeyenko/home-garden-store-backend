package com.homegarden.store.backend.dto;

import java.util.List;

public record CartResponseDto(

        Long cartId,

        Long userId,

        List<CartItemResponseDto> items) {
}
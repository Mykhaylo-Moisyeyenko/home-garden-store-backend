package com.homegarden.store.backend.dto;

import lombok.Builder;

@Builder
public record CartItemResponseDto(

        Long id,

        Long cartId,

        Long productId,

        String productName,

        Integer quantity){
}
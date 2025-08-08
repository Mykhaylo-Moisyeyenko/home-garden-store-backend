package com.homegarden.store.backend.dto;

import lombok.Builder;

@Builder
public record CartItemResponseDto(

        Long productId,

        String productName,

        Integer quantity){
}
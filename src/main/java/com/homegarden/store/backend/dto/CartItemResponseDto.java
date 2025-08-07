package com.homegarden.store.backend.dto;

public record CartItemResponseDto(

        Long id,

        Long cartId,

        Long productId,

        String productName,

        Integer quantity){
}
package com.homegarden.store.backend.dto;

public record CartItemResponseDTO(

        Long id,

        Long cartId,

        Long productId,

        String productName,

        Integer quantity){
}
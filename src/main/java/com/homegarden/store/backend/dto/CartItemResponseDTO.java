package com.homegarden.store.backend.dto;

//import java.math.BigDecimal;

public record CartItemResponseDTO(
        Long id,
        Long cartId,
        Long productId,
        String productName,
        Integer quantity){
}
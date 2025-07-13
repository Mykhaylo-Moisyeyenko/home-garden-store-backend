package com.homegarden.store.backend.dto;

//import java.math.BigDecimal;

public record CartItemResponseDTO(
        Long id,
        Long cartId,
        Long productId,
        String productName,
        Integer quantity){
        //В тех.задании нет цен в CartItem. Какое обоснование - для чего здесь цена?
        //BigDecimal price){
}

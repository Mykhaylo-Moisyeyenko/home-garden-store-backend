package com.homegarden.store.backend.dto;

import java.util.List;

public record CartResponseDTO(

        Long cartId,

        Long userId,

        List<CartItemResponseDTO> items) {
}
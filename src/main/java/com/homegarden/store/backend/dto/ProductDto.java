package com.homegarden.store.backend.dto;

import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {

    private Long productId;
    private String name;
    private String description;
    private BigDecimal price;
    private Long categoryId;
    private String imageUrl;
    private BigDecimal discountPrice;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
package com.homegarden.store.backend.dto;

import lombok.*;
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
    private Double price;
    private String categoryId;
    private String imageUrl;
    private Double discountPrice;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
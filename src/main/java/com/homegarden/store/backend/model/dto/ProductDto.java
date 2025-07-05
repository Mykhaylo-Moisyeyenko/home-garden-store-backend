package com.homegarden.store.backend.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class ProductDto {
    private Long productId;

    @NotBlank
    @Size(min = 1, max = 255)
    private String name;

    private String description;

    @NotNull
    @Positive
    private Double price;

    private Long categoryId;

    private String imageUrl;

    private Double discountPrice;

    private Timestamp createdAt;

    private Timestamp updatedAt;
}


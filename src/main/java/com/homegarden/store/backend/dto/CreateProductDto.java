package com.homegarden.store.backend.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProductDto {

    @NotBlank(message = "Product name can't be empty")
    @Size(min = 1, max = 255, message = "Product name must be between 1 and 255 characters")
    private String name;

    private String description;

    @NotNull(message = "Price cannot be null")
    @PositiveOrZero(message = "Price cannot be negative")
    private BigDecimal price;

    @NotNull(message = "Category Id can't be empty")
    @Positive(message = "Category Id must be more than 0")
    private Long categoryId;

    private String imageUrl;

    @PositiveOrZero(message = "Price cannot be negative")
    private BigDecimal discountPrice;
}
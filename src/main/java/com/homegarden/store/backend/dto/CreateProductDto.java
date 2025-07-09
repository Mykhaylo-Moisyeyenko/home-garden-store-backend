package com.homegarden.store.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductDto {

    @NotBlank(message = "Product name can't be empty")
    @Size(min = 1, max = 255, message = "Product name must be between 1 and 255 characters")
    private String name;

    private String description;

    @NotNull(message = "Price cannot be null")
    @PositiveOrZero(message = "Price cannot be negative")
    private Double price;

    @NotNull(message = "Category Id can't be empty")
    @Positive(message = "Category Id must be more than 0")
    private Long categoryId;
    private String imageUrl;
}
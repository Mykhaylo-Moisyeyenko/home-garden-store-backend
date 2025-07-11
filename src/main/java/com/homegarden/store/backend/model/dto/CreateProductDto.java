package com.homegarden.store.backend.model.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductDto {

        @NotBlank(message = "Product name can't be empty")
    @Size(min = 1, max = 255)
    private String name;

    private String description;

    @NotNull
    @PositiveOrZero(message = "Price cannot be negative")
    private Double price;

    @NotBlank(message = "Category Id can't be empty")
    private Long categoryId;
    private String imageUrl;
}
package com.homegarden.store.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long productId;
    private String name;
    private String description;
    private Double price;
    private Long categoryId;
    private String imageUrl;
    private Double discountPrice;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
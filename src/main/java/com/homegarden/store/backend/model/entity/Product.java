package com.homegarden.store.backend.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String ProductId;
    private String name;
    private String description;
    private Number price;
    private String categoryId;
    private String imageUrl;
    private String discountPrice;
    private String createdAt;
    private String updatedAt;

}

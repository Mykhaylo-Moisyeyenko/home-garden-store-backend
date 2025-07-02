package com.homegarden.store.backend.model.entity;

import jakarta.persistence.*;

public class Product {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    // Пример дополнительных полей:
    private String description;

    private Integer quantityInStock;

    // Пример для логики:
    private Boolean active = true;

    public void setCategory(Category category) {
    }
}

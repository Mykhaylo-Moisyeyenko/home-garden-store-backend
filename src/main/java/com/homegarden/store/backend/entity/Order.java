package com.homegarden.store.backend.entity;

import com.homegarden.store.backend.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<OrderItem> items = new ArrayList<>();

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private String deliveryAddress;

    private String contactPhone;

    private String deliveryMethod;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Status status = Status.CREATED;

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    BigDecimal orderTotalSum;
}
package com.homegarden.store.backend.entity;

import com.homegarden.store.backend.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long productId;

    //private Long user_id          int          not null references users (user_id),

    private LocalDateTime createdAt;

    private String deliveryAddress;

    private String contactPhone;

    private Status status;

    //    delivery_method  varchar(100) not null,

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.status = Status.CREATED;
    }
}
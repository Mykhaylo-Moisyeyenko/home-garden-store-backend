package com.homegarden.store.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.homegarden.store.backend.model.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long userId;

    private String name;
    private String email;
    private String passwordHash;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_USER;

    private String phoneNumber;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonManagedReference
    private List<Favorite> favorites;
}
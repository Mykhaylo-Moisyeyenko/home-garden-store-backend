package com.homegarden.store.backend.model.entity;

import com.homegarden.store.backend.model.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
//@Table(name = "local users")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor

public class User {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private Long userId;

    private String name;

    private String email;

    private String phoneNumber;

    private String passwordHash;

    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_USER;
}
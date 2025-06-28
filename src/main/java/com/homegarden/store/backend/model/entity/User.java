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
    private Long id;

    private String name;

    private String surname;

    private String email;

    private String password;

    private String postAddress; // БД будет искать по запросу post_address // postAddress

    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_USER;
}
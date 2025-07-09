package com.homegarden.store.backend.converter;

import com.homegarden.store.backend.dto.CreateUserRequestDTO;
import com.homegarden.store.backend.dto.UserResponseDTO;
import com.homegarden.store.backend.entity.User;
import com.homegarden.store.backend.enums.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserConverterTest {

    private UserConverter converter = new UserConverter();

    CreateUserRequestDTO createUserRequestDTO = new CreateUserRequestDTO(
            "Misha",
            "misha@gmail.com",
            "+11111111111",
            "9999999999999");

    User user = User.builder()
            .userId(1L)
            .name("Misha")
            .email("misha@gmail.com")
            .phoneNumber("+11111111111")
            .passwordHash("123456")
            .role(Role.ROLE_USER)
            .build();

    @Test
    void toEntityTest() {
        User userResult = converter.toEntity(createUserRequestDTO);

        assertNull(userResult.getUserId());
        assertEquals("Misha", userResult.getName());
        assertEquals("misha@gmail.com", userResult.getEmail());
        assertEquals("+11111111111", userResult.getPhoneNumber());
        assertEquals("9999999999999", userResult.getPasswordHash());
    }

    @Test
    void toDtoTest() {
        UserResponseDTO dtoResult = converter.toDto(user);

        assertEquals(1L, dtoResult.userId());
        assertEquals("Misha", dtoResult.name());
        assertEquals("misha@gmail.com", dtoResult.email());
        assertEquals("+11111111111", dtoResult.phoneNumber());
        assertEquals(Role.ROLE_USER, dtoResult.role());
    }
}
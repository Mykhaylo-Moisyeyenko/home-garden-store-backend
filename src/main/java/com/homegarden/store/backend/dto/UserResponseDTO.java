package com.homegarden.store.backend.dto;

import com.homegarden.store.backend.enums.Role;

public record UserResponseDTO(Long userId, String name, String email, String phoneNumber, Role role) {
}

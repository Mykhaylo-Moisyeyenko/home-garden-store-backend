package com.homegarden.store.backend.model.dto;

import com.homegarden.store.backend.model.enums.Role;

public record UserResponseDTO(Long userId, String name, String email, String phoneNumber, Role role) {
}

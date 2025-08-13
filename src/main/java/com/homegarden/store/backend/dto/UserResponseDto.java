package com.homegarden.store.backend.dto;

import com.homegarden.store.backend.enums.Role;
import lombok.Builder;

@Builder
public record UserResponseDto(Long userId,
                              String name,
                              String email,
                              String phoneNumber,
                              Role role) {
}

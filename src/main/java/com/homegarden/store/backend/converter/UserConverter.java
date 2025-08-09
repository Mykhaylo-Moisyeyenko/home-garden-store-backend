package com.homegarden.store.backend.converter;

import com.homegarden.store.backend.dto.CreateUserRequestDto;
import com.homegarden.store.backend.dto.UserResponseDto;
import com.homegarden.store.backend.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserConverter implements Converter<User, CreateUserRequestDto, UserResponseDto> {

    private final PasswordEncoder encoder;

    @Override
    public User toEntity(CreateUserRequestDto createUserRequestDto) {

        return User.builder()
                .name(createUserRequestDto.username())
                .email(createUserRequestDto.email())
                .phoneNumber(createUserRequestDto.phoneNumber())
                .passwordHash(encoder.encode(createUserRequestDto.password()))
                .build();
    }

    @Override
    public UserResponseDto toDto(User user) {

        return UserResponseDto.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .build();
    }
}
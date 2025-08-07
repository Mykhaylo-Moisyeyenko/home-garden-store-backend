package com.homegarden.store.backend.converter;

import com.homegarden.store.backend.dto.CreateUserRequestDto;
import com.homegarden.store.backend.dto.UserResponseDto;
import com.homegarden.store.backend.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverter implements Converter<User, CreateUserRequestDto, UserResponseDto> {

    @Override
    public User toEntity(CreateUserRequestDto createUserRequestDto) {

        return User
                .builder()
                .name(createUserRequestDto.username())
                .email(createUserRequestDto.email())
                .phoneNumber(createUserRequestDto.phoneNumber())
                .passwordHash(createUserRequestDto.password())
                .build();
    }

    @Override
    public UserResponseDto toDto(User user) {

        return new UserResponseDto(user.getUserId(), user.getName(), user.getEmail(), user.getPhoneNumber(), user.getRole());
    }
}
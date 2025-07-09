package com.homegarden.store.backend.converter;

import com.homegarden.store.backend.dto.CreateUserRequestDTO;
import com.homegarden.store.backend.dto.UserResponseDTO;
import com.homegarden.store.backend.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverter implements Converter<User, CreateUserRequestDTO, UserResponseDTO> {

    @Override
    public User toEntity(CreateUserRequestDTO createUserRequestDTO) {

        return User.builder()
                .name(createUserRequestDTO.username())
                .email(createUserRequestDTO.email())
                .phoneNumber(createUserRequestDTO.phoneNumber())
                .passwordHash(createUserRequestDTO.password())
                //change to PasswordEncoder later!!!!
                .build();
    }

    @Override
    public UserResponseDTO toDto(User user) {

        return new UserResponseDTO(user.getUserId(), user.getName(), user.getEmail(), user.getPhoneNumber(), user.getRole());
    }
}
package com.homegarden.store.backend.converter;

import com.homegarden.store.backend.model.dto.CreateUserRequestDTO;
import com.homegarden.store.backend.model.dto.UserResponseDTO;
import com.homegarden.store.backend.model.entity.User;

public class UserConverter implements Converter<User, CreateUserRequestDTO, UserResponseDTO> {

    @Override
    public User toEntity(CreateUserRequestDTO createUserRequestDTO) {

        return User.builder()
                .name(createUserRequestDTO.username())
                .email(createUserRequestDTO.email())
                .passwordHash(createUserRequestDTO.password())
                //change to PasswordEncoder later!!!!
                .build();
    }

    @Override
    public UserResponseDTO toDto(User user) {

        return new UserResponseDTO(user.getUserId(), user.getName());
    }
}

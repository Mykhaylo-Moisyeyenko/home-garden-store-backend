package com.homegarden.store.backend.converter;

import com.homegarden.store.backend.model.dto.CreateUserRequestDTO;
import com.homegarden.store.backend.model.dto.UserResponseDTO;
import com.homegarden.store.backend.model.entity.User;

public class UserConverter implements Converter<User, CreateUserRequestDTO, UserResponseDTO> {

    @Override
    public User toEntity(CreateUserRequestDTO createUserRequestDTO) {
        return null;
    }

    @Override
    public UserResponseDTO toDto(User User) {
        return new UserResponseDTO(User.getUserId(), User.getName());
    }
}

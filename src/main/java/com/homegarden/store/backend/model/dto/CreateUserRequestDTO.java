package com.homegarden.store.backend.model.dto;

public record CreateUserRequestDTO(String name, String email, String passwordHash) {

}

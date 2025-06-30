package com.homegarden.store.backend.controller;

import com.homegarden.store.backend.converter.UserConverter;
import com.homegarden.store.backend.model.dto.CreateUserRequestDTO;
import com.homegarden.store.backend.model.dto.UserResponseDTO;
import com.homegarden.store.backend.model.entity.User;
import com.homegarden.store.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
//@Slf4j
public class UserController {

    private UserService userService;

    private UserConverter converter;

    @PostMapping
    UserResponseDTO create(@RequestBody @Valid CreateUserRequestDTO userRequestDTO) {
        User entity = converter.toEntity(userRequestDTO);
        User user = userService.create(entity);
        UserResponseDTO dto = converter.toDto(user);
        return dto;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id) {
        User user = userService.getById(id);
        UserResponseDTO response = converter.toDto(user);
        return ResponseEntity.ok(response);
    }

}
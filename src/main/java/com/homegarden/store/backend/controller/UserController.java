package com.homegarden.store.backend.controller;

import com.homegarden.store.backend.converter.Converter;
import com.homegarden.store.backend.dto.CreateUserRequestDTO;
import com.homegarden.store.backend.dto.UpdateUserRequestDTO;
import com.homegarden.store.backend.dto.UserResponseDTO;
import com.homegarden.store.backend.entity.User;
import com.homegarden.store.backend.exception.UserAlreadyExistsException;
import com.homegarden.store.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")

public class UserController {

    private final UserService userService;

    private final Converter<User, CreateUserRequestDTO, UserResponseDTO> converter;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAll() {
        List<UserResponseDTO> response = userService.getAll().stream()
                .map(converter::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@RequestBody @Valid CreateUserRequestDTO userRequestDTO) {
        if (userService.existsByEmail(userRequestDTO.email())) {
            throw new UserAlreadyExistsException("User with email " + userRequestDTO.email() + " already exists");
        }
        User entity = converter.toEntity(userRequestDTO);
        User user = userService.create(entity);
        UserResponseDTO response = converter.toDto(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> update(
            @PathVariable Long userId,
            @RequestBody @Valid UpdateUserRequestDTO updateDto) {
        User updatedUser = userService.update(userId, updateDto);
        UserResponseDTO response = converter.toDto(updatedUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<UserResponseDTO> getById(@PathVariable Long id) {
        User user = userService.getById(id);
        UserResponseDTO response = converter.toDto(user);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
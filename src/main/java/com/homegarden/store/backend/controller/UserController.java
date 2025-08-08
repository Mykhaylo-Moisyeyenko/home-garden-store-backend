package com.homegarden.store.backend.controller;

import com.homegarden.store.backend.converter.Converter;
import com.homegarden.store.backend.dto.CreateUserRequestDto;
import com.homegarden.store.backend.dto.UpdateUserRequestDto;
import com.homegarden.store.backend.dto.UserResponseDto;
import com.homegarden.store.backend.entity.User;
import com.homegarden.store.backend.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;
    private final Converter<User, CreateUserRequestDto, UserResponseDto> converter;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAll() {
        List<UserResponseDto> response = userService
                .getAll()
                .stream()
                .map(converter::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> create(@RequestBody @Valid CreateUserRequestDto userRequestDTO) {
        User entity = converter.toEntity(userRequestDTO);
        entity.setPasswordHash(passwordEncoder.encode(userRequestDTO.password()));
        UserResponseDto response = converter.toDto(userService.create(entity));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseDto> update(
            @PathVariable @Min(1) Long userId,
            @RequestBody @Valid UpdateUserRequestDto updateDto) {
        User updatedUser = userService.update(userId, updateDto);

        return ResponseEntity.ok(converter.toDto(updatedUser));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<UserResponseDto> getById(@PathVariable @Min(1) Long id) {
        User user = userService.getById(id);

        return ResponseEntity.ok(converter.toDto(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Min(1) Long id) {
        userService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
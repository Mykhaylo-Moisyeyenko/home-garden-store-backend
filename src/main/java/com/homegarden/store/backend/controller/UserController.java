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

        return ResponseEntity.status(HttpStatus.CREATED).body(converter.toDto(userService.create(converter.toEntity(userRequestDTO))));
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

        return ResponseEntity.ok(converter.toDto(userService.getById(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Min(1) Long id) {
        userService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
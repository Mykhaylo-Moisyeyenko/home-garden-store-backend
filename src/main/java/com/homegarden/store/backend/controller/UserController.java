package com.homegarden.store.backend.controller;

import com.homegarden.store.backend.controller.api.UserControllerApi;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
@PreAuthorize("hasRole('ADMINISTRATOR')")
public class UserController implements UserControllerApi {

    private final UserService userService;
    private final Converter<User, CreateUserRequestDto, UserResponseDto> converter;

    @Override
    public ResponseEntity<List<UserResponseDto>> getAll() {
        List<UserResponseDto> response = userService.getAll()
                .stream()
                .map(converter::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @Override
    @PreAuthorize("isAnonymous() or hasRole('ADMINISTRATOR')")
    public ResponseEntity<UserResponseDto> create(@RequestBody @Valid CreateUserRequestDto userRequestDTO) {
        User user = userService.create(converter.toEntity(userRequestDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(converter.toDto(user));
    }

    @Override
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<UserResponseDto> update(@RequestBody @Valid UpdateUserRequestDto updateDto) {
        User updatedUser = userService.update(updateDto);
        return ResponseEntity.ok(converter.toDto(updatedUser));
    }

    @Override
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<UserResponseDto> getById(@PathVariable @Min(1) Long userId) {
        User user = userService.getById(userId);
        return ResponseEntity.ok(converter.toDto(user));
    }

    @Override
    public ResponseEntity<Void> delete(@PathVariable @Min(1) Long userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }
}

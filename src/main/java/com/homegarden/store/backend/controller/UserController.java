package com.homegarden.store.backend.controller;

import com.homegarden.store.backend.converter.UserConverter;
import com.homegarden.store.backend.exception.UserNotFoundException;
import com.homegarden.store.backend.model.dto.CreateUserRequestDTO;
import com.homegarden.store.backend.model.dto.UserResponseDTO;
import com.homegarden.store.backend.model.entity.User;
import com.homegarden.store.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/users")
//@Slf4j
public class UserController {

    private final UserService userService;

    private final UserConverter converter;

    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }

    @PostMapping
    UserResponseDTO create(@RequestBody @Valid CreateUserRequestDTO userRequestDTO) {
        User entity = converter.toEntity(userRequestDTO);
        User user = userService.create(entity);
        return converter.toDto(user);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<UserResponseDTO> getById(@PathVariable Long id) {
        User user = userService.getById(id);
        UserResponseDTO response = converter.toDto(user);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDTO> getByEmail(@PathVariable String email) {
        Optional<User> user = userService.getByEmail(email);
        UserResponseDTO response = converter.toDto(user.orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found")));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }

    public UserController(UserService userService) {
        this.userService = userService;
        this.converter = new UserConverter();
    }

}
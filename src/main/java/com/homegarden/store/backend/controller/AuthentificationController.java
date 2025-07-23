package com.homegarden.store.backend.controller;

import com.homegarden.store.backend.entity.User;
import com.homegarden.store.backend.security.model.LoginRequestDto;
import com.homegarden.store.backend.security.model.LoginResponseDto;
import com.homegarden.store.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class AuthentificationController {

    public final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {

        User user = userService.getByEmail(loginRequestDto.userEmail());

       if (passwordEncoder.matches(loginRequestDto.password(), user.getPasswordHash())){
           LoginResponseDto loginResponse = new LoginResponseDto("здесь будет передаваться jwt-token");
           return ResponseEntity.ok(loginResponse);
       }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
               .body(new LoginResponseDto("Password incorrect"));
    }
}
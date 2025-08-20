package com.homegarden.store.backend.controller;

import com.homegarden.store.backend.controller.api.AuthentificationControllerApi;
import com.homegarden.store.backend.dto.security.LoginRequestDto;
import com.homegarden.store.backend.dto.security.LoginResponseDto;
import com.homegarden.store.backend.service.security.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/v1/users/login")
public class AuthentificationController implements AuthentificationControllerApi {

    private final AuthenticationService authenticationService;

    @Override
    @PostMapping
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(authenticationService.authenticate(loginRequestDto));
    }
}

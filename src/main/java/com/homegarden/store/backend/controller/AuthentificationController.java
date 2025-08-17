package com.homegarden.store.backend.controller;

import com.homegarden.store.backend.controller.api.AuthentificationControllerApi;
import com.homegarden.store.backend.dto.security.LoginRequestDto;
import com.homegarden.store.backend.dto.security.LoginResponseDto;
import com.homegarden.store.backend.service.security.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthentificationController implements AuthentificationControllerApi {

    private final AuthenticationService authenticationService;

    @Override
    public ResponseEntity<LoginResponseDto> login(@Valid @org.springframework.web.bind.annotation.RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(authenticationService.authenticate(loginRequestDto));
    }
}

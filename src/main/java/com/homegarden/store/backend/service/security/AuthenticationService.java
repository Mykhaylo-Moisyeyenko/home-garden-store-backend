package com.homegarden.store.backend.service.security;

import com.homegarden.store.backend.dto.security.LoginRequestDto;
import com.homegarden.store.backend.dto.security.LoginResponseDto;

public interface AuthenticationService {

    LoginResponseDto authenticate(LoginRequestDto loginRequestDto);
}
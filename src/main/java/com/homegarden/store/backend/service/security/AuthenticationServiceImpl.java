package com.homegarden.store.backend.service.security;

import com.homegarden.store.backend.dto.security.LoginRequestDto;
import com.homegarden.store.backend.dto.security.LoginResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtService jwtService;

    @Override
    public LoginResponseDto authenticate(LoginRequestDto loginRequestDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.userEmail(),
                        loginRequestDto.password()));

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequestDto.userEmail());

        return new LoginResponseDto(jwtService.generateJwtToken(userDetails));
    }
}
package com.homegarden.store.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homegarden.store.backend.entity.User;
import com.homegarden.store.backend.dto.security.LoginRequestDto;
import com.homegarden.store.backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthentificationController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthentificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String loginUrl = "/login";

    @Test
    void login_withCorrectPassword_returnsJwtToken() throws Exception {
        String email = "user@example.com";
        String rawPassword = "validPass";
        String encodedPassword = "$2a$10$..."; // фиктивный hash

        User mockUser = new User();
        mockUser.setEmail(email);
        mockUser.setPasswordHash(encodedPassword);

        when(userService.getByEmail(email)).thenReturn(mockUser);
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

        LoginRequestDto request = new LoginRequestDto(email, rawPassword);

        mockMvc.perform(post(loginUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("здесь будет передаваться jwt-token"));
    }

    @Test
    void login_withIncorrectPassword_returnsUnauthorized() throws Exception {
        String email = "user@example.com";
        String rawPassword = "invalidPass";
        String encodedPassword = "$2a$10$...";

        User mockUser = new User();
        mockUser.setEmail(email);
        mockUser.setPasswordHash(encodedPassword);

        when(userService.getByEmail(email)).thenReturn(mockUser);
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);

        LoginRequestDto request = new LoginRequestDto(email, rawPassword);

        mockMvc.perform(post(loginUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.token").value("Password incorrect"));
    }
}
package com.homegarden.store.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homegarden.store.backend.dto.security.LoginRequestDto;
import com.homegarden.store.backend.dto.security.LoginResponseDto;
import com.homegarden.store.backend.service.security.AuthenticationService;
import com.homegarden.store.backend.service.security.JwtFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthentificationController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthentificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticationService authenticationService;

    @MockitoBean
    private JwtFilter jwtFilter;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private LoginRequestDto login = new LoginRequestDto("test@email.com",
            "password_111");

    private LoginResponseDto response = new LoginResponseDto("dfiajd;flkasdfjahsdfouahfg");

    @Test
    void loginTest() throws Exception {
        when(authenticationService.authenticate(login)).thenReturn(response);

        mockMvc.perform(post("/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("token").value("dfiajd;flkasdfjahsdfouahfg"));
    }
}
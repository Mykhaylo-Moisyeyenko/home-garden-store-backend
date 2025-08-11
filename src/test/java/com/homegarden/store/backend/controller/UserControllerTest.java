package com.homegarden.store.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homegarden.store.backend.converter.Converter;
import com.homegarden.store.backend.dto.CreateUserRequestDto;
import com.homegarden.store.backend.dto.UpdateUserRequestDto;
import com.homegarden.store.backend.dto.UserResponseDto;
import com.homegarden.store.backend.entity.User;
import com.homegarden.store.backend.service.UserService;
import com.homegarden.store.backend.service.security.JwtFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private Converter<User, CreateUserRequestDto, UserResponseDto> converter;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private JwtFilter jwtFilter;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private UserResponseDto userResponseDTO;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .userId(1L)
                .name("Test User")
                .email("test@example.com")
                .phoneNumber("1234567890")
                .build();

        userResponseDTO = new UserResponseDto(1L, "Test User", "test@example.com", "1234567890", null);
    }

    @Test
    void testGetAll() throws Exception {
        Mockito.when(userService.getAll()).thenReturn(List.of(user));
        Mockito.when(converter.toDto(user)).thenReturn(userResponseDTO);

        mockMvc.perform(get("/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("test@example.com"));
    }

    @Test
    void testCreate() throws Exception {
        CreateUserRequestDto dto = new CreateUserRequestDto("Test User", "test@example.com", "1234567890", "password");


        Mockito.when(converter.toEntity(dto)).thenReturn(user);
        Mockito.when(passwordEncoder.encode("pass")).thenReturn("encoded");
        user.setPasswordHash("encoded");
        Mockito.when(userService.create(any(User.class))).thenReturn(user);
        Mockito.when(converter.toDto(user)).thenReturn(userResponseDTO);

        mockMvc.perform(post("/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void testUpdate() throws Exception {
        UpdateUserRequestDto updateDto = new UpdateUserRequestDto("Updated", "9999999999");
        user.setName("Updated");
        user.setPhoneNumber("9999999999");
        Mockito.when(userService.update(updateDto)).thenReturn(user);
        Mockito.when(converter.toDto(user)).thenReturn(userResponseDTO);

        mockMvc.perform(put("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void testGetById() throws Exception {
        Mockito.when(userService.getById(1L)).thenReturn(user);
        Mockito.when(converter.toDto(user)).thenReturn(userResponseDTO);

        mockMvc.perform(get("/v1/users/id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete("/v1/users/1"))
                .andExpect(status().isNoContent());
        Mockito.verify(userService).delete(1L);
    }
}


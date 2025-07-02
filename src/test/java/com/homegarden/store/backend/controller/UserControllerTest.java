package com.homegarden.store.backend.controller;

import com.homegarden.store.backend.config.SecurityConfig;
import com.homegarden.store.backend.converter.UserConverter;
import com.homegarden.store.backend.model.dto.UserResponseDTO;
import com.homegarden.store.backend.model.entity.User;
import com.homegarden.store.backend.model.enums.Role;
import com.homegarden.store.backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserConverter converter;

    List<User> users;

    List<UserResponseDTO> userResponseDTOList;

    @BeforeEach
    void setUp() {
        User user1 = User.builder()
                .userId(1L)
                .name("Misha")
                .email("misha@gmail.com")
                .phoneNumber("+11111111111")
                .passwordHash("123456")
                .build();

        User user2 = User.builder()
                .userId(2L)
                .name("Sofia")
                .email("sofia@gmail.com")
                .phoneNumber("+222222222222")
                .passwordHash("987654")
                .build();

        users = List.of(user1, user2);

        UserResponseDTO userResponseDTO1 = new UserResponseDTO(
                user1.getUserId(),
                user1.getName(),
                user1.getEmail(),
                user1.getPhoneNumber(),
                Role.ROLE_USER);

        UserResponseDTO userResponseDTO2 = new UserResponseDTO(
                user2.getUserId(),
                user2.getName(),
                user2.getEmail(),
                user2.getPhoneNumber(),
                Role.ROLE_USER);

        userResponseDTOList = List.of(userResponseDTO1, userResponseDTO2);
    }

    @Test
    void getAllTest() throws Exception {
        when(userService.getAll()).thenReturn(users);
        when(converter.toDto(users.get(0))).thenReturn(userResponseDTOList.get(0));
        when(converter.toDto(users.get(1))).thenReturn(userResponseDTOList.get(1));

        mockMvc.perform(get("/v1/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].userId").value(1))
                .andExpect(jsonPath("$[0].name").value("Misha"))
                .andExpect(jsonPath("$[0].email").value("misha@gmail.com"))
                .andExpect(jsonPath("$[0].phoneNumber").value("+11111111111"))
                .andExpect(jsonPath("$[0].passwordHash").doesNotExist())
                .andExpect(jsonPath("$[1].userId").value(2))
                .andExpect(jsonPath("$[1].name").value("Sofia"))
                .andExpect(jsonPath("$[1].email").value("sofia@gmail.com"))
                .andExpect(jsonPath("$[1].phoneNumber").value("+222222222222"))
                .andExpect(jsonPath("$[1].passwordHash").doesNotExist());
    }

    @Test
    void createTest() {
    }

    @Test
    void getByIdTest() {
    }

    @Test
    void getByEmailTest() {
    }

    @Test
    void deleteTest() {
    }
}
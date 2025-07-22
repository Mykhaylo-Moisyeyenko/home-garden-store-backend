package com.homegarden.store.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homegarden.store.backend.converter.UserConverter;
import com.homegarden.store.backend.exception.UserNotFoundException;
import com.homegarden.store.backend.dto.CreateUserRequestDTO;
import com.homegarden.store.backend.dto.UserResponseDTO;
import com.homegarden.store.backend.entity.User;
import com.homegarden.store.backend.enums.Role;
import com.homegarden.store.backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserConverter converter;

    @Autowired
    private ObjectMapper objectMapper;

    List<User> users;

    List<UserResponseDTO> userResponseDTOList;

    User user1;
    UserResponseDTO userResponseDTO1;

    @BeforeEach
    void setUp() {
        user1 = User.builder()
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

        userResponseDTO1 = new UserResponseDTO(
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
    void createTest() throws Exception {
        CreateUserRequestDTO createUserRequestDTO = new CreateUserRequestDTO(
                "Misha", "misha@gmail.com", "+11111111111", "password6546198");
        when(userService.getByEmail(createUserRequestDTO.email())).thenReturn(false);
        when(converter.toEntity(createUserRequestDTO)).thenReturn(user1);
        when(userService.create(user1)).thenReturn(user1);
        when(converter.toDto(user1)).thenReturn(userResponseDTO1);

        mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserRequestDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.name").value("Misha"))
                .andExpect(jsonPath("$.email").value("misha@gmail.com"))
                .andExpect(jsonPath("$.phoneNumber").value("+11111111111"))
                .andExpect(jsonPath("$.role").value("ROLE_USER"))
                .andExpect(jsonPath("$.passwordHash").doesNotExist());
    }

    @Test
    void createTest_shouldFail_whenUserExists() throws Exception {
        CreateUserRequestDTO createUserRequestDTO = new CreateUserRequestDTO(
                "Misha", "misha@gmail.com", "+11111111111", "password6546198");
        when(userService.getByEmail(createUserRequestDTO.email())).thenReturn(true);

        mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserRequestDTO)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    void createTest_shouldFailValidation_whenAllFieldsInvalid() throws Exception {
        CreateUserRequestDTO createUserRequestDTO = new CreateUserRequestDTO(
                "", "", "", "");
        mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserRequestDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void getByIdTest() throws Exception {
        when(userService.getById(1L)).thenReturn(user1);
        when(converter.toDto(user1)).thenReturn(userResponseDTO1);

        mockMvc.perform(get("/v1/users/id/{id}", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.name").value("Misha"))
                .andExpect(jsonPath("$.email").value("misha@gmail.com"))
                .andExpect(jsonPath("$.phoneNumber").value("+11111111111"))
                .andExpect(jsonPath("$.role").value("ROLE_USER"))
                .andExpect(jsonPath("$.passwordHash").doesNotExist());
    }

    @Test
    void getById_shouldFail_whenUserNotFound() throws Exception {
        when(userService.getById(999L))
                .thenThrow(new UserNotFoundException("User with id " + 999L + " not found"));

        mockMvc.perform(get("/v1/users/id/{id}", 999L))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteTest() throws Exception {
        doNothing().when(userService).delete(1L);

        mockMvc.perform(delete("/v1/users/{id}", 1))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteTest_shouldFail_whenUserNotFound() throws Exception {
        doThrow(new UserNotFoundException("User with id " + 999L + " not found")).when(userService).delete(999L);

        mockMvc.perform(delete("/v1/users/{id}",999))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
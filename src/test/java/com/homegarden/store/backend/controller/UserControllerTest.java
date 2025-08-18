package com.homegarden.store.backend.controller;

import com.homegarden.store.backend.converter.Converter;
import com.homegarden.store.backend.dto.CreateUserRequestDto;
import com.homegarden.store.backend.dto.UpdateUserRequestDto;
import com.homegarden.store.backend.dto.UserResponseDto;
import com.homegarden.store.backend.entity.User;
import com.homegarden.store.backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Converter<User, CreateUserRequestDto, UserResponseDto> converter;

    @InjectMocks
    private UserController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAll_shouldReturnList() {
        User user = new User();
        UserResponseDto dto = UserResponseDto.builder().userId(1L).build();
        when(userService.getAll()).thenReturn(List.of(user));
        when(converter.toDto(user)).thenReturn(dto);

        ResponseEntity<List<UserResponseDto>> result = controller.getAll();

        assertThat(result.getBody()).contains(dto);
        verify(userService).getAll();
    }

    @Test
    void create_shouldReturnCreatedUser() {
        CreateUserRequestDto req = mock(CreateUserRequestDto.class);
        User user = new User();
        UserResponseDto dto = UserResponseDto.builder().userId(1L).build();
        when(converter.toEntity(req)).thenReturn(user);
        when(userService.create(user)).thenReturn(user);
        when(converter.toDto(user)).thenReturn(dto);

        ResponseEntity<UserResponseDto> result = controller.create(req);

        assertThat(result.getBody()).isEqualTo(dto);
    }

    @Test
    void update_shouldReturnUpdatedUser() {
        UpdateUserRequestDto req = mock(UpdateUserRequestDto.class);
        User user = new User();
        UserResponseDto dto = UserResponseDto.builder().userId(1L).build();
        when(userService.update(req)).thenReturn(user);
        when(converter.toDto(user)).thenReturn(dto);

        ResponseEntity<UserResponseDto> result = controller.update(req);

        assertThat(result.getBody()).isEqualTo(dto);
        verify(userService).update(req);
    }

    @Test
    void getById_shouldReturnUser() {
        User user = new User();
        UserResponseDto dto = UserResponseDto.builder().userId(1L).build();
        when(userService.getById(1L)).thenReturn(user);
        when(converter.toDto(user)).thenReturn(dto);

        ResponseEntity<UserResponseDto> result = controller.getById(1L);

        assertThat(result.getBody()).isEqualTo(dto);
        verify(userService).getById(1L);
    }

    @Test
    void delete_shouldCallService() {
        ResponseEntity<Void> result = controller.delete(1L);

        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        verify(userService).delete(1L);
    }
}

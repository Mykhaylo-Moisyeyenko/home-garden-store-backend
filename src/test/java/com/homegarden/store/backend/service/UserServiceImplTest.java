package com.homegarden.store.backend.service;

import com.homegarden.store.backend.dto.UpdateUserRequestDto;
import com.homegarden.store.backend.entity.User;
import com.homegarden.store.backend.exception.UserAlreadyExistsException;
import com.homegarden.store.backend.exception.UserNotFoundException;
import com.homegarden.store.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccessCheckService accessCheckService;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = User.builder()
                .userId(1L)
                .name("Test User")
                .email("test@example.com")
                .phoneNumber("1234567890")
                .build();
    }

    @Test
    void testCreateUser_Success() {
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);

        User created = userService.create(user);

        assertEquals(user, created);
        verify(userRepository).existsByEmail(user.getEmail());
        verify(userRepository).save(user);
    }

    @Test
    void testCreateUser_UserAlreadyExists() {
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> userService.create(user));
        verify(userRepository).existsByEmail(user.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> users = userService.getAll();

        assertEquals(1, users.size());
        assertEquals(user, users.get(0));
        verify(userRepository).findAll();
    }

    @Test
    void testGetUserById_UserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User found = userService.getById(1L);

        assertEquals(user, found);
        verify(userRepository).findById(1L);
    }

    @Test
    void testGetUserById_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getById(1L));
        verify(userRepository).findById(1L);
    }

    @Test
    void testUpdateUser() {
        UpdateUserRequestDto updateDto = new UpdateUserRequestDto("Updated Name", "9876543210");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(accessCheckService).checkAccess(user);
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User updated = userService.update(1L, updateDto);

        assertEquals("Updated Name", updated.getName());
        assertEquals("9876543210", updated.getPhoneNumber());
        verify(userRepository).save(user);
    }

    @Test
    void testDeleteUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.delete(1L);

        verify(userRepository).delete(user);
    }

    @Test
    void testGetByEmail_UserExists() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        User found = userService.getByEmail("test@example.com");

        assertEquals(user, found);
    }

    @Test
    void testGetByEmail_UserNotFound() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getByEmail("test@example.com"));
    }
}

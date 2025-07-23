//package com.homegarden.store.backend.service;
//
//import com.homegarden.store.backend.exception.UserNotFoundException;
//import com.homegarden.store.backend.entity.User;
//import com.homegarden.store.backend.enums.Role;
//import com.homegarden.store.backend.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class UserServiceImplTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @InjectMocks
//    private UserServiceImpl userServiceImpl;
//
//    private User user;
//
//    @BeforeEach
//    void setUp() {
//        user = User.builder()
//                .userId(1L)
//                .name("Misha")
//                .email("misha@gmail.com")
//                .phoneNumber("123456789")
//                .passwordHash("99999999")
//                .role(Role.ROLE_USER)
//                .build();
//    }
//
//    @Test
//    void getAllTest() {
//        when(userRepository.findAll()).thenReturn(List.of(user));
//
//        List<User> usersActual = userServiceImpl.getAll();
//
//        assertEquals(1, usersActual.size());
//        assertEquals(user, usersActual.get(0));
//        verify(userRepository).findAll();
//    }
//
//    @Test
//    void createTest() {
//        when(userRepository.save(user)).thenReturn(user);
//
//        User userCreated = userServiceImpl.create(user);
//
//        assertEquals(userCreated, user);
//        verify(userRepository).save(user);
//    }
//
//    @Test
//    void getByIdTest() {
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//
//        User userActual = userServiceImpl.getById(1L);
//
//        assertEquals(userActual, user);
//        verify(userRepository).findById(1L);
//    }
//
//    @Test
//    void getByIdTest_shouldFail_WhenUserNotFound() {
//        when(userRepository.findById(1L))
//                .thenThrow(new UserNotFoundException("User with id " + 1L + " not found"));
//
//        assertThrows(UserNotFoundException.class, ()-> userServiceImpl.getById(1L));
//        verify(userRepository).findById(1L);
//    }
//
//    @Test
//    void deleteTest() {
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//        doNothing().when(userRepository).delete(user);
//
//        userServiceImpl.delete(1L);
//
//        verify(userRepository).findById(1L);
//        verify(userRepository).delete(user);
//    }
//
//    @Test
//    void existsByEmail() {
//        when(userRepository.existsByEmail("misha@gmail.com")).thenReturn(true);
//
//        boolean result = userServiceImpl.existsByEmail("misha@gmail.com");
//
//        assertTrue(result);
//        verify(userRepository).existsByEmail("misha@gmail.com");
//    }
//
//    @Test
//    void existsByEmailTest_shouldReturnFalse_WhenUserNotFound() {
//        when(userRepository.existsByEmail("false@gmail.com")).thenReturn(false);
//
//        boolean result = userServiceImpl.existsByEmail("false@gmail.com");
//
//        assertFalse(result);
//        verify(userRepository).existsByEmail("false@gmail.com");
//    }
//
//    @Test
//    void existsByIdTest() {
//        when(userRepository.existsById(1L)).thenReturn(true);
//
//        boolean result = userServiceImpl.existsById(1L);
//
//        assertTrue(result);
//        verify(userRepository).existsById(1L);
//    }
//
//    @Test
//    void existsByIdTest_shouldReturnFalse_WhenUserNotFound() {
//        when(userRepository.existsById(6L)).thenReturn(false);
//
//        boolean result = userServiceImpl.existsById(6L);
//
//        assertFalse(result);
//        verify(userRepository).existsById(6L);
//    }
//}
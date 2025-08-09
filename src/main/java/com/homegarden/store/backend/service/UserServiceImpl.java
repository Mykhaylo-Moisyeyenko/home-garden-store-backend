package com.homegarden.store.backend.service;

import com.homegarden.store.backend.dto.UpdateUserRequestDto;
import com.homegarden.store.backend.entity.User;
import com.homegarden.store.backend.enums.Role;
import com.homegarden.store.backend.exception.UserAlreadyExistsException;
import com.homegarden.store.backend.exception.UserNotFoundException;
import com.homegarden.store.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User create(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("User with email " + user.getEmail() + " already exists");
        }

        return userRepository.save(user);
    }

    @Override
    public User update(UpdateUserRequestDto updateDto) {
        User user = getCurrentUser();
        user.setName(updateDto.username());
        user.setPhoneNumber(updateDto.phoneNumber());

        return userRepository.save(user);
    }

    @Override
    public User getById(Long userId) {
        User user = getCurrentUser();

        if (user.getRole().equals(Role.ROLE_USER)) {
            return userRepository.findById(user.getUserId())
                    .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));
        }

        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));
    }

    @Override
    public void delete(Long id) {
        userRepository.delete(getById(id));
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
    }

    @Override
    public User getCurrentUser() {
        return getByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
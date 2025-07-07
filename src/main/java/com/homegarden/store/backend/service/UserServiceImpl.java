package com.homegarden.store.backend.service;

import com.homegarden.store.backend.exception.UserNotFoundException;
import com.homegarden.store.backend.model.dto.UpdateUserRequestDTO;
import com.homegarden.store.backend.model.entity.User;
import com.homegarden.store.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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
    public User create(User User) {
        return userRepository.save(User);
    }

    @Override
    public User update(Long userId, UpdateUserRequestDTO updateDto) {
        User user = getById(userId);
        user.setName(updateDto.username());
        user.setPhoneNumber(updateDto.phoneNumber());
        return userRepository.save(user);
    }

    @Override
    public User getById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));
    }

    @Override
    public void delete(Long id) {
        User user = getById(id);
        userRepository.delete(user);
    }

    @Override
    public boolean existsByEmail(String email){ return userRepository.existsByEmail(email); }

    @Override
    public boolean existsById(Long id) { return userRepository.existsById(id); }
}
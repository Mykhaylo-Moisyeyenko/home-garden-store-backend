package com.homegarden.store.backend.service;

import com.homegarden.store.backend.dto.UpdateUserRequestDTO;
import com.homegarden.store.backend.entity.User;
import com.homegarden.store.backend.exception.UserAlreadyExistsException;
import com.homegarden.store.backend.exception.UserNotFoundException;
import com.homegarden.store.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final AccessCheckService accessCheckService;

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
    public User update(Long userId, UpdateUserRequestDTO updateDto) {
        User user = getById(userId);
        accessCheckService.checkAccess(user);
        user.setName(updateDto.username());
        user.setPhoneNumber(updateDto.phoneNumber());
        return userRepository.save(user);
    }

    @Override
    public User getById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));
        accessCheckService.checkAccess(user);
        return user;
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

    @Override
    public User getByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found")); }
}
package com.homegarden.store.backend.service;

import com.homegarden.store.backend.dto.UpdateUserRequestDto;
import com.homegarden.store.backend.entity.User;
import java.util.List;

public interface UserService {

    List<User> getAll();

    User create(User User);

    User update(Long userId, UpdateUserRequestDto updateDto);

    User getById(Long id);

    boolean existsByEmail(String email);

    boolean existsById(Long id);

    void delete(Long id);

    User getByEmail(String email);
}
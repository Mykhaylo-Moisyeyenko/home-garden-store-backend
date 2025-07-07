package com.homegarden.store.backend.service;

import com.homegarden.store.backend.model.dto.UpdateUserRequestDTO;
import com.homegarden.store.backend.model.entity.User;

import java.util.List;

public interface UserService {

    List<User> getAll();

    User create(User User);

    User update(Long userId, UpdateUserRequestDTO updateDto);


    User getById(Long id);

    boolean existsByEmail(String email);

    void delete(Long id);
}
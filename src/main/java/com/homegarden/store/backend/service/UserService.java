package com.homegarden.store.backend.service;

import com.homegarden.store.backend.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAll();

    User create(User localUser);

    User getById(Long id);

    Optional<User> getByEmail(String email);
}
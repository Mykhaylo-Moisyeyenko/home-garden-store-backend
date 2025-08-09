package com.homegarden.store.backend.service;

import com.homegarden.store.backend.dto.UpdateUserRequestDto;
import com.homegarden.store.backend.entity.User;
import java.util.List;

public interface UserService {

    List<User> getAll();

    User create(User User);

    User update(UpdateUserRequestDto updateDto);

    User getById(Long id);

    void delete(Long id);

    User getByEmail(String email);

    User getCurrentUser();
}
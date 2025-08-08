package com.homegarden.store.backend.repository;

import com.homegarden.store.backend.entity.Cart;
import com.homegarden.store.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {

    boolean existsCartByUser(User user);

    List<Cart> findByUser_UserId(Long userUserId);

    Cart getByUser(User user);
}
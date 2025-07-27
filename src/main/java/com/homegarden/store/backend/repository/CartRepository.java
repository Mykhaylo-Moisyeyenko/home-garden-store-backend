package com.homegarden.store.backend.repository;

import com.homegarden.store.backend.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByUser_UserId(Long userUserId);
}
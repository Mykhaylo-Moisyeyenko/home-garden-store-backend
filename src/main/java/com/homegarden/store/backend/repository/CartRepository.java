package com.homegarden.store.backend.repository;

import com.homegarden.store.backend.entity.Cart;
import com.homegarden.store.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    boolean existsCartByUser(User user);

    List<Cart> findByUser_UserId(Long userUserId);

    Cart getByUser(User user);
}
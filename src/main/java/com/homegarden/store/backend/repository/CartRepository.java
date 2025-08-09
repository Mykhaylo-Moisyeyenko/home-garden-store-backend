package com.homegarden.store.backend.repository;

import com.homegarden.store.backend.entity.Cart;
import com.homegarden.store.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    boolean existsCartByUser(User user);

    Cart getByUser(User user);
}
package com.homegarden.store.backend.repository;

import com.homegarden.store.backend.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
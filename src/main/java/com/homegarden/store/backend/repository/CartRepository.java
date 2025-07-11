package com.homegarden.store.backend.repository;

import com.homegarden.store.backend.model.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}


package com.homegarden.store.backend.repository;

import com.homegarden.store.backend.model.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findByUser_UserId(Long userId);

    Optional<Favorite> findByUser_UserIdAndProduct_ProductId(Long userId, Long productId);

    void deleteByUser_UserIdAndProduct_ProductId(Long userId, Long productId);

    Optional<Favorite> findByProduct_ProductId(Long productId);
}
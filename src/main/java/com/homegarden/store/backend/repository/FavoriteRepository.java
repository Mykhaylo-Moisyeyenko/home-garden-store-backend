package com.homegarden.store.backend.repository;

import com.homegarden.store.backend.entity.Favorite;
import com.homegarden.store.backend.entity.Product;
import com.homegarden.store.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findAllByUser_UserId(Long userUserId);

    boolean existsByUser_AndProduct(User user, Product product);

    void deleteByUserAndProduct(User user, Product product);

    List<Favorite> findAllByUser(User user);
}
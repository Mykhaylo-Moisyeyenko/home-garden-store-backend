package com.homegarden.store.backend.service;

import com.homegarden.store.backend.entity.Favorite;
import com.homegarden.store.backend.entity.Product;
import com.homegarden.store.backend.entity.User;
import com.homegarden.store.backend.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final ProductService productService;
    private final UserService userService;

    @Override
    public List<Favorite> getAll() {
        User user = userService.getCurrentUser();

        return favoriteRepository.findAllByUser(user);
    }

    @Override
    public void addToFavorites(Favorite favorite) {
        User user = userService.getCurrentUser();
        Product product = productService.getById(favorite.getProduct().getProductId());

        if (!favoriteRepository.existsByUserAndProduct(user, product)) {
            favoriteRepository.save(favorite);
        }
    }

    @Override
    @Transactional
    public void removeFromFavorites(Favorite favorite) {
        User user = userService.getCurrentUser();

        favoriteRepository.deleteByUserAndProduct(
                user,
                favorite.getProduct());
    }
}
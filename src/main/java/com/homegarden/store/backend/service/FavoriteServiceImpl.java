package com.homegarden.store.backend.service;

import com.homegarden.store.backend.entity.Favorite;
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
    private final AccessCheckService accessCheckService;

    @Override
    public List<Favorite> getAll(Long userId) {
        User user = userService.getById(userId);

        return favoriteRepository.findAllByUser(user);
    }

    @Override
    public void addToFavorites(Favorite favorite) {
        productService.getById(favorite.getProduct().getProductId());
        if (!favoriteRepository
                .existsByUser_AndProduct(favorite.getUser(), favorite.getProduct())) {
            favoriteRepository.save(favorite);
        }
    }

    @Override
    @Transactional
    public void removeFromFavorites(Favorite favorite) {
        User user = userService.getById(favorite.getUser().getUserId());
        accessCheckService.checkAccess(user);

        favoriteRepository.deleteByUserAndProduct(
                favorite.getUser(),
                favorite.getProduct());
    }
}
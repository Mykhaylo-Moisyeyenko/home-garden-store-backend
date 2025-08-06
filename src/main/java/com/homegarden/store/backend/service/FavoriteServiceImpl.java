package com.homegarden.store.backend.service;

import com.homegarden.store.backend.entity.Favorite;
import com.homegarden.store.backend.exception.ProductNotFoundException;
import com.homegarden.store.backend.exception.UserNotFoundException;
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
    public List<Favorite> getAll(Long userId) {
        if (!userService.existsById(userId)) {
            throw new UserNotFoundException("User with id " + userId + " doesn't exists");
        }
        return favoriteRepository.findAllByUser_UserId(userId);
    }

    @Override
    public void addToFavorites(Favorite favorite) {
        if (!userService.existsById(favorite.getUser().getUserId())) {
            throw new UserNotFoundException("User not found");
        }
        if (!productService.existsById(favorite.getProduct().getProductId())) {
            throw new ProductNotFoundException("Product not found");
        }

        if (!favoriteRepository
                .existsByUser_AndProduct(favorite.getUser(), favorite.getProduct())) {
            favoriteRepository.save(favorite);
        }
    }

    @Override
    @Transactional
    public void removeFromFavorites(Favorite favorite) {
        favoriteRepository.deleteByUserAndProduct(favorite.getUser(), favorite.getProduct());
    }
}
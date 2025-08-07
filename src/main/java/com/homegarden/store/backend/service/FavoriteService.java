package com.homegarden.store.backend.service;

import com.homegarden.store.backend.entity.Favorite;
import java.util.List;

public interface FavoriteService {

    List<Favorite> getAll(Long userId);

    void addToFavorites(Favorite favorite);

    void removeFromFavorites(Favorite favorite);
}
package com.homegarden.store.backend.service;

import com.homegarden.store.backend.entity.Favorite;
import com.homegarden.store.backend.entity.Product;
import com.homegarden.store.backend.entity.User;
import com.homegarden.store.backend.exception.ProductNotFoundException;
import com.homegarden.store.backend.exception.UserNotFoundException;
import com.homegarden.store.backend.repository.FavoriteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FavoriteServiceImplTest {

    private FavoriteRepository favoriteRepository;
    private ProductService productService;
    private UserService userService;
    private FavoriteServiceImpl favoriteService;

    private final Long userId = 1L;
    private final Long productId = 101L;

    private Favorite favorite;

    @BeforeEach
    void setUp() {
        favoriteRepository = mock(FavoriteRepository.class);
        productService = mock(ProductService.class);
        userService = mock(UserService.class);
        favoriteService = new FavoriteServiceImpl(favoriteRepository, productService, userService);

        favorite = Favorite.builder()
                .user(User.builder().userId(userId).build())
                .product(Product.builder().productId(productId).build())
                .build();
    }

    @Test
    @DisplayName("getAll: should return list of favorites when user exists")
    void getAll_whenUserExists_shouldReturnFavorites() {
        when(userService.existsById(userId)).thenReturn(true);
        when(favoriteRepository.findAllByUser_UserId(userId)).thenReturn(List.of(favorite));

        List<Favorite> result = favoriteService.getAll(userId);

        assertEquals(1, result.size());
        assertEquals(userId, result.get(0).getUser().getUserId());
        assertEquals(productId, result.get(0).getProduct().getProductId());
        verify(userService).existsById(userId);
        verify(favoriteRepository).findAllByUser_UserId(userId);
    }

    @Test
    @DisplayName("getAll: should throw exception when user does not exist")
    void getAll_whenUserNotExists_shouldThrowException() {
        when(userService.existsById(userId)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> favoriteService.getAll(userId));
        verify(userService).existsById(userId);
        verifyNoInteractions(favoriteRepository);
    }

    @Test
    @DisplayName("addToFavorites: should save when not already exists")
    void addToFavorites_whenValid_shouldSave() {
        when(userService.existsById(userId)).thenReturn(true);
        when(productService.existsById(productId)).thenReturn(true);
        when(favoriteRepository.existsByUser_AndProduct(favorite.getUser(), favorite.getProduct()))
                .thenReturn(false);

        favoriteService.addToFavorites(favorite);

        verify(favoriteRepository).save(favorite);
    }

    @Test
    @DisplayName("addToFavorites: should not save if already exists")
    void addToFavorites_whenAlreadyExists_shouldNotSave() {
        when(userService.existsById(userId)).thenReturn(true);
        when(productService.existsById(productId)).thenReturn(true);
        when(favoriteRepository.existsByUser_AndProduct(favorite.getUser(), favorite.getProduct()))
                .thenReturn(true);

        favoriteService.addToFavorites(favorite);

        verify(favoriteRepository, never()).save(any());
    }

    @Test
    @DisplayName("addToFavorites: should throw if user not found")
    void addToFavorites_whenUserNotFound_shouldThrow() {
        when(userService.existsById(userId)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> favoriteService.addToFavorites(favorite));
        verify(favoriteRepository, never()).save(any());
    }

    @Test
    @DisplayName("addToFavorites: should throw if product not found")
    void addToFavorites_whenProductNotFound_shouldThrow() {
        when(userService.existsById(userId)).thenReturn(true);
        when(productService.existsById(productId)).thenReturn(false);

        assertThrows(ProductNotFoundException.class, () -> favoriteService.addToFavorites(favorite));
        verify(favoriteRepository, never()).save(any());
    }

    @Test
    @DisplayName("removeFromFavorites: should call delete")
    void removeFromFavorites_shouldDelete() {
//        favoriteService.removeFromFavorites(favorite);
//
//        verify(favoriteRepository).delete(favorite);
    }
}

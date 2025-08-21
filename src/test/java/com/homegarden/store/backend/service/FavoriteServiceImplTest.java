package com.homegarden.store.backend.service;

import com.homegarden.store.backend.entity.Favorite;
import com.homegarden.store.backend.entity.Product;
import com.homegarden.store.backend.entity.User;
import com.homegarden.store.backend.exception.ProductNotFoundException;
import com.homegarden.store.backend.repository.FavoriteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceImplTest {

    @Mock
    FavoriteRepository favoriteRepository;

    @Mock
    ProductService productService;

    @Mock
    UserService userService;

    @InjectMocks
    FavoriteServiceImpl favoriteService;

    private final Long userId = 1L;
    private final Long productId = 101L;

    private Favorite favorite;
    private User user;
    private Product product;

    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteServiceImpl(
                favoriteRepository,
                productService,
                userService);

        user = User.builder()
                .userId(userId)
                .build();

        product = Product.builder()
                .productId(productId)
                .build();

        favorite = Favorite.builder()
                .user(user)
                .product(product)
                .build();
    }

    @Test
    @DisplayName("getAll: should return list of favorites")
    void getAll_shouldReturnFavorites() {
        doReturn(user).when(userService).getCurrentUser();
        when(favoriteRepository.findAllByUser(user)).thenReturn(List.of(favorite));

        List<Favorite> result = favoriteService.getAll();

        assertEquals(1, result.size());
        assertEquals(userId, result.get(0).getUser().getUserId());
        assertEquals(productId, result.get(0).getProduct().getProductId());
        verify(userService).getCurrentUser();
        verify(favoriteRepository).findAllByUser(user);
    }

    @Test
    @DisplayName("addToFavorites: should save when not yet exists")
    void addToFavorites_whenValid_shouldSave() {
        doReturn(user).when(userService).getCurrentUser();
        when(productService.getById(productId)).thenReturn(product);
        when(favoriteRepository.existsByUserAndProduct(favorite.getUser(), favorite.getProduct()))
                .thenReturn(false);

        favoriteService.addToFavorites(favorite);

        verify(userService).getCurrentUser();
        verify(productService).getById(productId);
        verify(favoriteRepository).save(favorite);
    }

    @Test
    @DisplayName("addToFavorites: should not save if already exists")
    void addToFavorites_whenAlreadyExists_shouldNotSave() {
        doReturn(user).when(userService).getCurrentUser();
        when(productService.getById(productId)).thenReturn(product);
        when(favoriteRepository.existsByUserAndProduct(favorite.getUser(), favorite.getProduct()))
                .thenReturn(true);

        favoriteService.addToFavorites(favorite);

        verify(userService).getCurrentUser();
        verify(productService).getById(productId);
        verify(favoriteRepository, never()).save(any());
    }

    @Test
    @DisplayName("addToFavorites: should throw if product not found")
    void addToFavorites_whenProductNotFound_shouldThrow() {
        doReturn(user).when(userService).getCurrentUser();
        when(productService.getById(productId))
                .thenThrow(new ProductNotFoundException("Product not found with id: " + productId));

        assertThrows(ProductNotFoundException.class, () -> favoriteService.addToFavorites(favorite));

        verify(userService).getCurrentUser();
        verify(productService).getById(productId);
        verify(favoriteRepository, never()).save(any());
    }

    @Test
    @DisplayName("removeFromFavorites: should call delete")
    void removeFromFavorites_shouldDelete() {
        doReturn(user).when(userService).getCurrentUser();
        doNothing().when(favoriteRepository).deleteByUserAndProduct(user, favorite.getProduct());

        favoriteService.removeFromFavorites(favorite);

        verify(userService).getCurrentUser();
        verify(favoriteRepository).deleteByUserAndProduct(user, favorite.getProduct());
    }
}
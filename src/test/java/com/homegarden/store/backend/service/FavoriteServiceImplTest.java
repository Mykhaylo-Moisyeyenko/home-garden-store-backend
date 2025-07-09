package com.homegarden.store.backend.service;

import com.homegarden.store.backend.exception.ProductNotFoundException;
import com.homegarden.store.backend.exception.UserNotFoundException;
import com.homegarden.store.backend.dto.FavoriteDto;
import com.homegarden.store.backend.entity.Favorite;
import com.homegarden.store.backend.entity.Product;
import com.homegarden.store.backend.entity.User;
import com.homegarden.store.backend.repository.FavoriteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class FavoriteServiceImplTest {

    private FavoriteRepository favoriteRepository;
    private ProductService productService;
    private UserService userService;
    private FavoriteServiceImpl favoriteService;
    FavoriteDto dto;

    @BeforeEach
    void setUp() {
        favoriteRepository = mock(FavoriteRepository.class);
        productService = mock(ProductService.class);
        userService = mock(UserService.class);
        favoriteService = new FavoriteServiceImpl(favoriteRepository, productService, userService);
        dto = new FavoriteDto(1L, 101L);
    }

    @Test
    void getAll_shouldReturnListOfDto() {
        Favorite entity = Favorite.builder().userId(1L).productId(101L).build();
        when(favoriteRepository.findByUserId(1L)).thenReturn(List.of(entity));

        List<FavoriteDto> result = favoriteService.getAll(1L);

        assertEquals(1, result.size());
        assertEquals(101L, result.get(0).productId());
        verify(favoriteRepository).findByUserId(1L);
    }

    @Test
    void addToFavorites_shouldSaveIfNotExists() {
        when(userService.getById(1L)).thenReturn(User.builder().userId(1L).build());
        when(productService.getById(101L)).thenReturn(Product.builder().productId(101L).build());
        when(favoriteRepository.findByUserIdAndProductId(1L, 101L))
                .thenReturn(Optional.empty());

        favoriteService.addToFavorites(dto);

        verify(favoriteRepository).save(any(Favorite.class));
    }

    @Test
    void addToFavorites_shouldNotSave_IfUserNotExists() {
        when(userService.getById(1L)).thenThrow(new UserNotFoundException("User not found"));

        assertThrows(UserNotFoundException.class, () -> favoriteService.addToFavorites(dto));
        verify(productService, never()).getById(anyLong());
        verify(favoriteRepository, never()).findByUserIdAndProductId(anyLong(), anyLong());
        verify(favoriteRepository, never()).save(any());
    }

    @Test
    void addToFavorites_shouldNotSave_IfProductNotExists() {
        when(userService.getById(1L)).thenReturn(User.builder().userId(1L).build());
        when(productService.getById(101L)).thenThrow(new ProductNotFoundException("Product not found"));

        assertThrows(ProductNotFoundException.class, () -> favoriteService.addToFavorites(dto));
        verify(favoriteRepository, never()).findByUserIdAndProductId(anyLong(), anyLong());
        verify(favoriteRepository, never()).save(any());
    }

    @Test
    void addToFavorites_shouldNotSave_IfUserHasProductInFavorites() {
        when(userService.getById(1L)).thenReturn(User.builder().userId(1L).build());
        when(productService.getById(101L)).thenReturn(Product.builder().productId(101L).build());
        when(favoriteRepository.findByUserIdAndProductId(1L, 101L))
                .thenReturn(Optional.of(Favorite.builder().userId(1L).productId(101L).build()));

        favoriteService.addToFavorites(dto);

        verify(favoriteRepository, never()).save(any());
    }

    @Test
    void removeFromFavorites_shouldCallDelete() {
        favoriteService.removeFromFavorites(dto);

        verify(favoriteRepository).deleteByUserIdAndProductId(1L, 101L);
    }
}
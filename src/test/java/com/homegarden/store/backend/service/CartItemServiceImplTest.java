package com.homegarden.store.backend.service;

import com.homegarden.store.backend.entity.Cart;
import com.homegarden.store.backend.entity.CartItem;
import com.homegarden.store.backend.entity.Product;
import com.homegarden.store.backend.entity.User;
import com.homegarden.store.backend.exception.CartItemNotFoundException;
import com.homegarden.store.backend.exception.CartNotFoundException;
import com.homegarden.store.backend.exception.ProductNotFoundException;
import com.homegarden.store.backend.repository.CartItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartItemServiceImplTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private CartService cartService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private CartItemServiceImpl cartItemServiceImpl;

    CartItem cartItem = CartItem.builder()
            .cart(Cart.builder().cartId(1L).build())
            .product(Product.builder().productId(1L).name("TestProduct").build())
            .quantity(10)
            .build();

    Cart cart = new Cart(1L, new ArrayList<>(), User.builder().userId(1L).build());
    Product product = Product.builder().productId(1L).name("TestProduct").build();
    CartItem cartItemSaved = new CartItem(1L, cart, product, 10);
    CartItem updatedCartItem = new CartItem(1L, cart, product, 20);

    @Test
    void createTestSuccessful() {
        when(cartService.getById(1L)).thenReturn(cart);
        when(productService.getById(1L)).thenReturn(product);
        when(cartItemRepository.save(cartItem)).thenReturn(cartItemSaved);

        CartItem actual = cartItemServiceImpl.create(cartItem);

        assertEquals(cartItemSaved, actual);
        assertEquals(cart, actual.getCart());
        assertEquals(product, actual.getProduct());
        assertEquals(cartItemSaved.getQuantity(), actual.getQuantity());
        verify(cartService).getById(1L);
        verify(productService).getById(1L);
        verify(cartItemRepository).save(cartItem);
    }

    @Test
    void createTestFailWhenCartNotFound() {
        doThrow(new CartNotFoundException("Cart not found"))
                .when(cartService).getById(1L);

        assertThrows(CartNotFoundException.class, () -> cartItemServiceImpl.create(cartItem));

        verify(cartService, times(1)).getById(1L);
        verify(cartItemRepository, never()).save(cartItem);
    }

    @Test
    void createTestFailWhenProductNotFound() {
        when(cartService.getById(1L)).thenReturn(cart);
        doThrow(new ProductNotFoundException("Product not found"))
                .when(productService).getById(1L);

        assertThrows(ProductNotFoundException.class, () -> cartItemServiceImpl.create(cartItem));

        verify(cartService, times(1)).getById(1L);
        verify(productService, times(1)).getById(1L);
        verify(cartItemRepository, never()).save(cartItem);
    }

    @Test
    void updateQuantityTestSuccessful() {
        when(cartItemRepository.findById(1L)).thenReturn(Optional.of(cartItemSaved));
        cartItemSaved.setQuantity(20);
        when(cartItemRepository.save(cartItemSaved)).thenReturn(updatedCartItem);

        CartItem actual = cartItemServiceImpl.updateQuantity(1L, 20);

        assertEquals(updatedCartItem, actual);
        assertEquals(updatedCartItem.getCart(), actual.getCart());
        assertEquals(updatedCartItem.getProduct(), actual.getProduct());
        assertEquals(updatedCartItem.getQuantity(), actual.getQuantity());
        verify(cartItemRepository).findById(1L);
        verify(cartItemRepository).save(cartItemSaved);
    }

    @Test
    void updateQuantityTestFailWhenCartItemNotFound() {
        when(cartItemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CartItemNotFoundException.class,
                () -> cartItemServiceImpl.updateQuantity(1L, 20));

        verify(cartItemRepository,times(1)).findById(1L);
        verify(cartItemRepository, never()).save(cartItemSaved);
    }

    @Test
    void getByIdTestSuccessful() {
        when(cartItemRepository.findById(1L)).thenReturn(Optional.of(cartItemSaved));

        CartItem actual = cartItemServiceImpl.getById(1L);

        assertEquals(cartItemSaved, actual);
        verify(cartItemRepository,times(1)).findById(1L);
    }

    @Test
    void getByIdTestFailWhenCartItemNotFound() {
        when(cartItemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CartItemNotFoundException.class, () -> cartItemServiceImpl.getById(1L));

        verify(cartItemRepository,times(1)).findById(1L);
    }

    @Test
    void getAllTest() {
        when(cartItemRepository.findAll()).thenReturn(List.of(cartItemSaved));

        List<CartItem> result = cartItemServiceImpl.getAll();

        assertEquals(List.of(cartItemSaved), result);
        verify(cartItemRepository,times(1)).findAll();
    }

    @Test
    void deleteTest() {
        when(cartItemRepository.findById(1L)).thenReturn(Optional.of(cartItemSaved));
        doNothing().when(cartItemRepository).deleteById(1L);

        cartItemServiceImpl.delete(1L);

        verify(cartItemRepository,times(1)).findById(1L);
        verify(cartItemRepository,times(1)).deleteById(1L);
    }
}
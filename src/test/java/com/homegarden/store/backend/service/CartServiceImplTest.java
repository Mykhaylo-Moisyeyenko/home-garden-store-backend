package com.homegarden.store.backend.service;

import com.homegarden.store.backend.entity.Cart;
import com.homegarden.store.backend.entity.CartItem;
import com.homegarden.store.backend.entity.Product;
import com.homegarden.store.backend.entity.User;
import com.homegarden.store.backend.exception.CartAlreadyExistsException;
import com.homegarden.store.backend.repository.CartRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartRepository cartRepositoryTest;

    @Mock
    private UserService userServiceTest;

    @InjectMocks
    private CartServiceImpl cartServiceImpl;

    private final User user = User.builder().userId(1L).build();

    private final CartItem cartItem = CartItem.builder()
            .cartItemId(1L)
            .product(Product.builder().productId(1L).build())
            .quantity(10)
            .build();

    private final Cart emptyCart = Cart.builder().user(user).items(new ArrayList<>()).build();

    @Test
    void createTestSuccessful() {
        when(userServiceTest.getCurrentUser()).thenReturn(user);
        // Implementation uses getByUser(...), not existsCartByUser(...)
        when(cartRepositoryTest.getByUser(user)).thenReturn(null);
        when(cartRepositoryTest.save(emptyCart)).thenReturn(emptyCart);

        Cart result = cartServiceImpl.create(emptyCart);

        assertEquals(emptyCart, result);
        verify(userServiceTest).getCurrentUser();
        verify(cartRepositoryTest).getByUser(user);
        verify(cartRepositoryTest).save(emptyCart);
        verify(cartRepositoryTest, never()).existsCartByUser(any());
    }

    @Test
    void createTestWhenCartAlreadyExists() {
        when(userServiceTest.getCurrentUser()).thenReturn(user);
        // Simulate existing cart in DB
        Cart existing = Cart.builder().user(user).items(new ArrayList<>()).build();
        when(cartRepositoryTest.getByUser(user)).thenReturn(existing);

        assertThrows(CartAlreadyExistsException.class, () -> cartServiceImpl.create(emptyCart));

        verify(userServiceTest).getCurrentUser();
        verify(cartRepositoryTest).getByUser(user);
        verify(cartRepositoryTest, never()).save(any());
        verify(cartRepositoryTest, never()).existsCartByUser(any());
    }

    @Test
    void addCartItem_createsCartWhenAbsent() {
        when(userServiceTest.getCurrentUser()).thenReturn(user);
        when(cartRepositoryTest.getByUser(user)).thenReturn(null);
        when(cartRepositoryTest.save(any(Cart.class)))
                .thenAnswer(inv -> inv.getArgument(0, Cart.class));

        CartItem item = CartItem.builder()
                .product(Product.builder().productId(99L).build())
                .quantity(1)
                .build();

        Cart saved = cartServiceImpl.addCartItem(item);

        assertNotNull(saved);
        assertEquals(user, saved.getUser());
        assertEquals(1, saved.getItems().size());
        assertSame(saved, saved.getItems().get(0).getCart());

        verify(cartRepositoryTest).getByUser(user);
        verify(cartRepositoryTest).save(any(Cart.class));
    }

    @Test
    void getAllCartItems_returnsEmptyListWhenNoCart() {
        when(userServiceTest.getCurrentUser()).thenReturn(user);
        when(cartRepositoryTest.getByUser(user)).thenReturn(null);

        List<CartItem> items = cartServiceImpl.getAllCartItems();

        assertNotNull(items);
        assertTrue(items.isEmpty());
        verify(cartRepositoryTest).getByUser(user);
    }

    @Test
    void delete_ignoresWhenNoCart() {
        when(userServiceTest.getCurrentUser()).thenReturn(user);
        when(cartRepositoryTest.getByUser(user)).thenReturn(null);

        cartServiceImpl.delete();

        verify(cartRepositoryTest, never()).delete(any());
    }
}
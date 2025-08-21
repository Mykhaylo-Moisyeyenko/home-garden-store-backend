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

    private final Cart cart1 = Cart.builder().user(user).items(new ArrayList<>()).build();
    private final User user1 = User.builder().userId(1L).cart(cart1).build();

    private final Cart cart2 = Cart.builder().user(user).items(List.of(cartItem)).build();
    private final User user2 = User.builder().userId(2L).cart(cart2).build();

    private final Cart cart3 = Cart.builder().user(user).items(List.of(cartItem)).build();

    private final Cart cart4 = Cart.builder().items(new ArrayList<>(List.of(cartItem))).build();
    private final User user4 = User.builder().userId(4L).cart(cart4).build();


    @Test
    void createTestSuccessful() {
        when(userServiceTest.getCurrentUser()).thenReturn(user);
        when(cartRepositoryTest.getByUser(user)).thenReturn(null);
        when(cartRepositoryTest.save(emptyCart)).thenReturn(emptyCart);

        Cart result = cartServiceImpl.create(emptyCart);

        assertEquals(emptyCart, result);
        verify(userServiceTest).getCurrentUser();
        verify(cartRepositoryTest).getByUser(user);
        verify(cartRepositoryTest).save(emptyCart);
    }

    @Test
    void createTestWhenCartAlreadyExists() {
        when(userServiceTest.getCurrentUser()).thenReturn(user);
        Cart existing = Cart.builder().user(user).items(new ArrayList<>()).build();
        when(cartRepositoryTest.getByUser(user)).thenReturn(existing);

        assertThrows(CartAlreadyExistsException.class, () -> cartServiceImpl.create(emptyCart));

        verify(userServiceTest).getCurrentUser();
        verify(cartRepositoryTest).getByUser(user);
        verify(cartRepositoryTest, never()).save(any());
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

    @Test
    void getByUserTest() {
        when(cartRepositoryTest.getByUser(user)).thenReturn(emptyCart);

        Cart actual = cartServiceImpl.getByUser(user);

        assertEquals(emptyCart, actual);
        verify(cartRepositoryTest).getByUser(user);
    }

    @Test
    void updateTest() {
        Cart cartFromFront = Cart.builder().user(user1).items(List.of(cartItem)).build();
        when(userServiceTest.getCurrentUser()).thenReturn(user1);
        Cart cartCurrentUser = Cart.builder().user(user1).items(new ArrayList<>()).build();
        cartCurrentUser.setItems(cartFromFront.getItems());
        when(cartRepositoryTest.save(any(Cart.class))).thenReturn(cartCurrentUser);

        Cart actual = cartServiceImpl.update(cartFromFront);

        assertEquals(cartCurrentUser, actual);
        verify(userServiceTest).getCurrentUser();
        verify(cartRepositoryTest).save(any(Cart.class));
    }

    @Test
    void addCartItemTest() {
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

        verify(cartRepositoryTest).getByUser(user);
        verify(cartRepositoryTest).save(any(Cart.class));
    }

    @Test
    void updateCartItemQuantityTest() {
        when(userServiceTest.getCurrentUser()).thenReturn(user2);
        CartItem cartItem3 = cart3.getItems().get(0);
        cartItem3.setQuantity(20);
        when(cartRepositoryTest.save(cart3)).thenReturn(cart3);

        Cart actual = cartServiceImpl.updateCartItemQuantity(1L, 20);

        assertEquals(cart3, actual);
        verify(userServiceTest, times(2)).getCurrentUser();
        verify(cartRepositoryTest).save(any(Cart.class));
    }

    @Test
    void deleteCartItem() {
        when(userServiceTest.getCurrentUser()).thenReturn(user4);
        when(cartRepositoryTest.save(any(Cart.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Cart actual = cartServiceImpl.deleteCartItem(1L);
        assertTrue(actual.getItems().isEmpty());
        verify(userServiceTest, atLeast(1)).getCurrentUser();
        verify(cartRepositoryTest).save(any(Cart.class));
    }
}
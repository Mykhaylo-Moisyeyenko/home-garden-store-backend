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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartRepository cartRepositoryTest;

    @Mock
    private UserService userServiceTest;

    @InjectMocks
    private CartServiceImpl cartServiceImpl;

    private User user = User.builder().userId(1L).build();

    private CartItem cartItem = CartItem.builder()
            .cartItemId(1L)
            .cart(Cart.builder().cartId(1L).build())
            .product(Product.builder().productId(1L).build())
            .quantity(10)
            .build();

    private Cart cart = Cart.builder().user(user).build();

    private Cart cartSaved = new Cart(1L, List.of(cartItem), user);


    @Test
    void createTestSuccessful() {
        doReturn(user).when(userServiceTest).getCurrentUser();
        when(cartRepositoryTest.existsCartByUser(user)).thenReturn(false);
        when(cartRepositoryTest.save(cart)).thenReturn(cartSaved);

        Cart result = cartServiceImpl.create(cart);

        assertEquals(cartSaved, result);
        verify(userServiceTest, times(1)).getCurrentUser();
        verify(cartRepositoryTest, times(1)).existsCartByUser(user);
        verify(cartRepositoryTest, times(1)).save(cart);
    }

    @Test
    void createTestWhenCartAlreadyExists() {
        doReturn(user).when(userServiceTest).getCurrentUser();
        when(cartRepositoryTest.existsCartByUser(user)).thenReturn(true);

        assertThrows(CartAlreadyExistsException.class, () -> cartServiceImpl.create(cart));

        verify(userServiceTest, times(1)).getCurrentUser();
        verify(cartRepositoryTest, times(1)).existsCartByUser(user);
        verify(cartRepositoryTest, never()).save(cart);
    }
//}
//
//    @Test
//    void getAllTest() {
//        when(cartRepositoryTest.findAll()).thenReturn(List.of(cartSaved));
//
//        List<Cart> result = cartServiceImpl.getAll();
//
//        assertEquals(List.of(cartSaved), result);
//        verify(cartRepositoryTest, times(1)).findAll();
//    }
//
    @Test
    void deleteTest() {
        doReturn(user).when(userServiceTest).getCurrentUser();
        doNothing().when(cartRepositoryTest).delete(user.getCart());

        cartServiceImpl.delete();

        verify(userServiceTest, times(1)).getCurrentUser();
        verify(cartRepositoryTest, times(1)).delete(user.getCart());
    }
}
package com.homegarden.store.backend.service;

import com.homegarden.store.backend.repository.OrderItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderItemServiceImplTest {
    
    @Mock
    private OrderItemRepository orderItemRepositoryTest;
    
    @InjectMocks
    private OrderItemServiceImpl orderItemServiceImplTest;

    @Test
    void getTopCancelledProductsTest() {
        List<Object[]> listOfProducts = List.of(
                new Object[]{1L,"Product1", 1L},
                new Object[]{2L,"Product2", 2L}
        );

        when(orderItemRepositoryTest.findTopCancelledProducts()).thenReturn(listOfProducts);

        List<Object[]> result = orderItemServiceImplTest.getTopCancelledProducts();

        assertEquals(listOfProducts.size(), result.size());
        verify(orderItemRepositoryTest, times(1)).findTopCancelledProducts();
    }
}
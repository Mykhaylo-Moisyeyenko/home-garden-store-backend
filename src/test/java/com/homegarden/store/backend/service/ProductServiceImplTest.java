package com.homegarden.store.backend.service;

import com.homegarden.store.backend.entity.Category;
import com.homegarden.store.backend.entity.Product;
import com.homegarden.store.backend.exception.ProductNotFoundException;
import com.homegarden.store.backend.exception.ProductUsedInOrdersException;
import com.homegarden.store.backend.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepositoryTest;

    @Mock
    private OrderService orderServiceTest;

    @InjectMocks
    private ProductServiceImpl productServiceTest;

    Product product;
    Product productSaved;

    @BeforeEach
    void setUp() {
        Category category = Category.builder()
                .categoryId(1L)
                .name("Category test")
                .products(new ArrayList<>())
                .build();

        product = Product.builder()
                .name("Product test")
                .description("Description test")
                .price(new BigDecimal("11.11"))
                .category(category)
                .imageUrl("Test Image")
                .discountPrice(new BigDecimal("9.99"))
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .updatedAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        productSaved = Product.builder()
                .productId(1L)
                .name("Product test")
                .description("Description test")
                .price(new BigDecimal("11.11"))
                .category(category)
                .imageUrl("Test Image")
                .discountPrice(new BigDecimal("9.99"))
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .updatedAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();
    }

    @Test
    void createTest() {
        when(productRepositoryTest.save(any(Product.class))).thenReturn(productSaved);

        Product result = productServiceTest.create(product);

        assertEquals(productSaved, result);
        verify(productRepositoryTest, times(1)).save(any(Product.class));
    }

    @Test
    void getAllTest() {
        when(productRepositoryTest.findAll(any(Specification.class))).thenReturn(List.of(productSaved));

        List<Product> result = productServiceTest.getAll(
                1L,
                new BigDecimal("1.11"),
                new BigDecimal("9.99"),
                true,
                "ASC");

        assertEquals(List.of(productSaved), result);
        verify(productRepositoryTest, times(1)).findAll(any(Specification.class));
    }

    @Test
    void getByIdTestSuccessfully() {
        when(productRepositoryTest.findById(1L)).thenReturn(Optional.of(productSaved));

        Product result = productServiceTest.getById(1L);

        assertEquals(productSaved, result);
        verify(productRepositoryTest, times(1)).findById(1L);
    }

    @Test
    void getByIdTestProductNotFound() {
        when(productRepositoryTest.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productServiceTest.getById(1L));

        verify(productRepositoryTest, times(1)).findById(1L);
    }

    @Test
    void testDeleteProductSuccessful() {
        when(productRepositoryTest.findById(1L)).thenReturn(Optional.of(productSaved));
        when(orderServiceTest.isProductUsedInOrders(1L)).thenReturn(false);

        productServiceTest.delete(1L);

        verify(productRepositoryTest, times(1)).findById(1L);
        verify(productRepositoryTest, times(1)).deleteById(1L);
        verify(orderServiceTest, times(1)).isProductUsedInOrders(1L);
    }

    @Test
    void testDeleteProductNotFound() {
        when(productRepositoryTest.findById(1L)).thenReturn(Optional.of(productSaved));
        when(orderServiceTest.isProductUsedInOrders(1L)).thenReturn(true);

        assertThrows(ProductUsedInOrdersException.class, () -> productServiceTest.delete(1L));

        verify(productRepositoryTest, times(1)).findById(1L);
        verify(productRepositoryTest, never()).deleteById(1L);
        verify(orderServiceTest, times(1)).isProductUsedInOrders(1L);
    }

    @Test
    void testUpdateProduct_ProductExists() {
        Product updatedProduct = new Product();
        updatedProduct.setProductId(1L);
        updatedProduct.setName("Updated Name");
        updatedProduct.setDescription("Updated Description");
        updatedProduct.setPrice(new BigDecimal("10.00"));

        when(productRepositoryTest.findById(1L)).thenReturn(Optional.of(productSaved));
        when(productRepositoryTest.save(any(Product.class))).thenReturn(updatedProduct);

        Product result = productServiceTest.update(updatedProduct);

        assertEquals(updatedProduct.getName(), result.getName());
        assertEquals(updatedProduct.getDescription(), result.getDescription());
        assertEquals(updatedProduct.getPrice(), result.getPrice());
        verify(productRepositoryTest, times(1)).findById(1L);
        verify(productRepositoryTest, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdateProduct_ProductNotFound() {
        when(productRepositoryTest.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productServiceTest.update(productSaved));

        verify(productRepositoryTest, times(1)).findById(1L);
        verify(productRepositoryTest, never()).save(any(Product.class));
    }

    @Test
    void existsByIdTest() {
        when(productRepositoryTest.existsById(1L)).thenReturn(true);

        boolean result = productServiceTest.existsById(1L);

        assertTrue(result);
        verify(productRepositoryTest, times(1)).existsById(1L);
    }
}
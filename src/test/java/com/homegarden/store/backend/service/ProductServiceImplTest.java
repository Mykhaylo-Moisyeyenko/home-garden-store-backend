//package com.homegarden.store.backend.service;
//
//import com.homegarden.store.backend.exception.ProductNotFoundException;
//import com.homegarden.store.backend.entity.Product;
//import com.homegarden.store.backend.repository.ProductRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class ProductServiceImplTest {
//
//    @Mock
//    private ProductRepository productRepository;
//
//    @InjectMocks
//    private ProductServiceImpl productService;
//
//    private Product product;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        product = new Product();
//        product.setProductId(1L);
//        product.setName("Test Product");
//        product.setDescription("Test Description");
//        product.setPrice(100.0);
//    }
//
//    @Test
//    void testCreateProduct() {
//        when(productRepository.save(product)).thenReturn(product);
//
//        Product created = productService.create(product);
//
//        assertEquals(product, created);
//        verify(productRepository, times(1)).save(product);
//    }
//
//    @Test
//    void testGetAllProducts() {
//        List<Product> products = Arrays.asList(product);
//        when(productRepository.findAll()).thenReturn(products);
//
//        List<Product> result = productService.getAll();
//
//        assertEquals(1, result.size());
//        assertEquals(product, result.get(0));
//        verify(productRepository, times(1)).findAll();
//    }
//
//    @Test
//    void testGetById_ProductExists() {
//        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
//
//        Product found = productService.getById(1L);
//
//        assertEquals(product, found);
//        verify(productRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    void testGetById_ProductNotFound() {
//        when(productRepository.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(ProductNotFoundException.class, () -> productService.getById(1L));
//
//        verify(productRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    void testDeleteProduct() {
//        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
//
//        productService.delete(1L);
//
//        verify(productRepository, times(1)).findById(1L);
//        verify(productRepository, times(1)).deleteById(1L);
//    }
//
//    @Test
//    void testUpdateProduct_ProductExists() {
//        Product updatedProduct = new Product();
//        updatedProduct.setProductId(1L);
//        updatedProduct.setName("Updated Name");
//        updatedProduct.setDescription("Updated Description");
//        updatedProduct.setPrice(200.0);
//
//        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
//        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);
//
//        Product result = productService.update(updatedProduct);
//
//        assertEquals(updatedProduct.getName(), result.getName());
//        assertEquals(updatedProduct.getDescription(), result.getDescription());
//        assertEquals(updatedProduct.getPrice(), result.getPrice());
//        verify(productRepository, times(1)).findById(1L);
//        verify(productRepository, times(1)).save(any(Product.class));
//    }
//
//    @Test
//    void testUpdateProduct_ProductNotFound() {
//        when(productRepository.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(ProductNotFoundException.class, () -> productService.update(product));
//
//        verify(productRepository, times(1)).findById(1L);
//        verify(productRepository, never()).save(any(Product.class));
//    }
//}
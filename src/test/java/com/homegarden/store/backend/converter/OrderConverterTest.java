package com.homegarden.store.backend.converter;

import com.homegarden.store.backend.dto.CreateOrderItemRequestDTO;
import com.homegarden.store.backend.dto.CreateOrderRequestDTO;
import com.homegarden.store.backend.dto.OrderItemResponseDTO;
import com.homegarden.store.backend.dto.OrderResponseDTO;
import com.homegarden.store.backend.entity.Order;
import com.homegarden.store.backend.entity.OrderItem;
import com.homegarden.store.backend.entity.Product;
import com.homegarden.store.backend.entity.User;
import com.homegarden.store.backend.enums.Status;
import com.homegarden.store.backend.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderConverterTest {

    private ProductService productService;
    private OrderConverter orderConverter;

    @BeforeEach
    void setUp() {
        productService = mock(ProductService.class);
        orderConverter = new OrderConverter(productService);
    }

    @Test
    void testToEntity() {
        CreateOrderItemRequestDTO itemDTO = new CreateOrderItemRequestDTO(1L, 2);
        CreateOrderRequestDTO dto = new CreateOrderRequestDTO(
                List.of(itemDTO),
                "Pushkina Street",
                "Pickup");

        Product product = Product.builder()
                .productId(1L)
                .price(BigDecimal.valueOf(100.00))
                .build();

        when(productService.getById(1L)).thenReturn(product);

        Order order = orderConverter.toEntity(dto);

        assertNotNull(order);
        assertEquals("Pushkina Street", order.getDeliveryAddress());
        assertEquals("Pickup", order.getDeliveryMethod());
        assertEquals(1, order.getItems().size());

        OrderItem orderItem = order.getItems().get(0);
        assertEquals(product, orderItem.getProduct());
        assertEquals(2, orderItem.getQuantity());
        assertEquals(product.getPrice(), orderItem.getPriceAtPurchase());
    }

    @Test
    void testToDto() {
        Product product = Product.builder()
                .productId(1L)
                .build();

        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(2);
        item.setPriceAtPurchase(BigDecimal.valueOf(100.00));

        Order order = Order.builder()
                .orderId(1L)
                .user(User.builder().userId(42L).build())
                .deliveryAddress("Pushkina Street")
                .deliveryMethod("Pickup")
                .contactPhone("1234567890")
                .status(Status.CREATED)
                .items(List.of(item))
                .build();

        OrderResponseDTO dto = orderConverter.toDto(order);

        assertNotNull(dto);
        assertEquals(1L, dto.orderId());
        assertEquals(42L, dto.userId());
        assertEquals("Pushkina Street", dto.deliveryAddress());
        assertEquals("Pickup", dto.deliveryMethod());
        assertEquals("1234567890", dto.contactPhone());
        assertEquals(Status.CREATED, dto.status());

        List<OrderItemResponseDTO> itemDTOs = dto.items();
        assertEquals(1, itemDTOs.size());
        assertEquals(1L, itemDTOs.get(0).productId());
        assertEquals(2, itemDTOs.get(0).quantity());
        assertEquals(BigDecimal.valueOf(100.00), itemDTOs.get(0).priceAtPurchase());
    }
}



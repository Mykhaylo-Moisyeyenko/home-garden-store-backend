package com.homegarden.store.backend.converter;

import com.homegarden.store.backend.model.dto.CartItemResponseDTO;
import com.homegarden.store.backend.model.dto.CreateCartItemRequestDTO;
import com.homegarden.store.backend.model.entity.Cart;
import com.homegarden.store.backend.model.entity.CartItem;
import com.homegarden.store.backend.model.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class CartItemConverter implements Converter<CartItem, CreateCartItemRequestDTO, CartItemResponseDTO> {

    @Override
    public CartItem toEntity(CreateCartItemRequestDTO dto) {
        Cart cart = new Cart();
        cart.setCartId(dto.cartId());

        Product product = new Product();
        product.setProductId(dto.productId());

        return CartItem.builder()
                .cart(cart)
                .product(product)
                .quantity(dto.quantity())
                .build();
    }

    @Override
    public CartItemResponseDTO toDto(CartItem item) {
        return new CartItemResponseDTO(
                item.getId(),
                item.getCart().getCartId(),
                item.getProduct().getProductId(),
                item.getProduct().getName(),
                item.getQuantity(),
                item.getPrice()
        );
    }
}


package com.homegarden.store.backend.service;

import com.homegarden.store.backend.entity.Cart;
import com.homegarden.store.backend.entity.CartItem;
import com.homegarden.store.backend.entity.Order;
import com.homegarden.store.backend.entity.OrderItem;
import com.homegarden.store.backend.exception.InvalidOrderItemQuantityException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartToOrderServiceImpl implements CartToOrderService {
    private final CartService cartService;
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final CartItemService cartItemService;

    @Override
    @Transactional
    public Order create(Order order) {
        Cart cart = cartService.getByUserId(order.getUser().getUserId());
        List<Long> allCartProductIds = cart.getItems()
                .stream()
                .map((cartItem) -> {
                    return cartItem.getProduct().getProductId();
                }).toList();

        for (OrderItem orderItem : order.getItems()) {
            Long productId = orderItem.getProduct().getProductId();
            if (!allCartProductIds.contains(productId)) {
                throw new IllegalArgumentException("Product id " + productId + " is not in the Cart");
            }
        }

        Order newOrder = orderService.create(order);

        for (OrderItem orderItem : order.getItems()) {
            Long productId = orderItem.getProduct().getProductId();
            orderItemService.save(orderItem);
            CartItem cartItemToDelete = cart.getItems()
                    .stream()
                    .filter((item) -> {
                        return item.getProduct().getProductId() == productId;
                    })
                    .findFirst()
                    .get();
            //checking the quantity
            int quantity = cartItemToDelete.getQuantity() - orderItem.getQuantity();
            if (quantity == 0) {
                cartItemService.delete(cartItemToDelete.getCartItemId());
                cart.deleteCartItem(cartItemToDelete);
            } else if (quantity > 0) {
                cartItemToDelete.setQuantity(quantity);
                cartItemService.updateQuantity(cartItemToDelete.getCartItemId(), quantity);
            } else {
                throw new InvalidOrderItemQuantityException("Invalid order item quantity");
            }
        }
        return newOrder;
    }
}
package com.homegarden.store.backend.exception;

public class OrderItemsListIsEmptyException extends RuntimeException {

    public OrderItemsListIsEmptyException(String message) {
        super(message);
    }
}
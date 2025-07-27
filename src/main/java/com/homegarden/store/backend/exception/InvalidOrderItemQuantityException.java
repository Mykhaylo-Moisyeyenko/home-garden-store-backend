package com.homegarden.store.backend.exception;

public class InvalidOrderItemQuantityException extends RuntimeException {
    public InvalidOrderItemQuantityException(String message) {
        super(message);
    }
}

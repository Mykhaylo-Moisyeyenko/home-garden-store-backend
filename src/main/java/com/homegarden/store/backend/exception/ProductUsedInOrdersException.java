package com.homegarden.store.backend.exception;

public class ProductUsedInOrdersException extends RuntimeException {

    public ProductUsedInOrdersException(String message) {
        super(message);
    }
}
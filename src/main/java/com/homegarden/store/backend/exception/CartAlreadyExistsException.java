package com.homegarden.store.backend.exception;

public class CartAlreadyExistsException extends RuntimeException {

    public CartAlreadyExistsException(String message) {
        super(message);
    }
}
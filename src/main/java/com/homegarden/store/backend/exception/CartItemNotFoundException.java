package com.homegarden.store.backend.exception;

public class CartItemNotFoundException extends RuntimeException{

    public CartItemNotFoundException(String message) {super(message);
    }
}
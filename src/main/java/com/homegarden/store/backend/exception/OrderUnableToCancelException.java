package com.homegarden.store.backend.exception;

public class OrderUnableToCancelException extends RuntimeException {

    public OrderUnableToCancelException(String message) {
        super(message);
    }
}
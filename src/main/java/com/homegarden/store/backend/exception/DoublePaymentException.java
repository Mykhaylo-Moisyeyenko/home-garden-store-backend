package com.homegarden.store.backend.exception;

public class DoublePaymentException extends RuntimeException {

    public DoublePaymentException(String message) {
        super(message);
    }
}
package com.homegarden.store.backend.exception;

public class ReportBadRequestException extends RuntimeException {

    public ReportBadRequestException(String message) {
        super(message);
    }
}
package com.homegarden.store.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {UserNotFoundException.class, CategoryNotFoundException.class,
            OrderNotFoundException.class, ProductNotFoundException.class,
            CartNotFoundException.class, CartItemNotFoundException.class})
    public String handleNotFoundException(Exception exception){
        return exception.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public String handleBadRequestException(MethodArgumentTypeMismatchException exception){
        return "HTTP method argument invalid type";  //when we get a String id instead of a Long id in Postman
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleInvalidArgumentException(MethodArgumentNotValidException exception){
        String message = exception.getBindingResult().getFieldErrors().stream()
                .map(field -> field.getField() + ": " + field.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return "HTTP body fields not valid: " + message;
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(value = {UserAlreadyExistsException.class, OrderUnableToCancelException.class,
            ProductUsedInOrdersException.class})
    public String handleConflict(Exception exception) {
        return exception.getMessage();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @ExceptionHandler(FavoriteAlreadyExistsException.class)
    public String handleConflict(FavoriteAlreadyExistsException ex) {
        return ex.getMessage();
    }
}
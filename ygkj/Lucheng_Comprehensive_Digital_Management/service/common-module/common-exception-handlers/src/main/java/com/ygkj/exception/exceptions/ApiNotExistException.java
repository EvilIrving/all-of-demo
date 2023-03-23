package com.ygkj.exception.exceptions;

public class ApiNotExistException extends RuntimeException {

    public ApiNotExistException() {
        super();
    }

    public ApiNotExistException(String message) {
        super(message);
    }
}

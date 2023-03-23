package com.ygkj.exception.exceptions;

public class OperationNotAllowedException extends RuntimeException {

    public OperationNotAllowedException() {
    }

    public OperationNotAllowedException(String message) {
        super(message);
    }

    public OperationNotAllowedException(Throwable cause) {
        super(cause);
    }

    public OperationNotAllowedException(String message, Throwable cause) {
        super(message, cause);
    }

    protected OperationNotAllowedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

package com.ygkj.exception.exceptions;

public class PermissionDeniedException extends RuntimeException {

    public PermissionDeniedException() {
    }

    public PermissionDeniedException(String message) {
        super(message);
    }

    public PermissionDeniedException(Throwable cause) {
        super(cause);
    }

    public PermissionDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    protected PermissionDeniedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

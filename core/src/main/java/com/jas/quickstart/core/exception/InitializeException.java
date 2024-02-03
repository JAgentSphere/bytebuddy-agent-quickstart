package com.jas.quickstart.core.exception;

/**
 * @author ReaJason
 * @since 2024/1/31
 */
public class InitializeException extends RuntimeException {
    public InitializeException() {
        super();
    }

    public InitializeException(String message) {
        super(message);
    }

    public InitializeException(String message, Throwable cause) {
        super(message, cause);
    }
}

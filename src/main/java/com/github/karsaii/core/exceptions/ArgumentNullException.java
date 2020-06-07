package com.github.karsaii.core.exceptions;

public class ArgumentNullException extends IllegalArgumentException {
    public ArgumentNullException(String s) {
        super(s);
    }

    public ArgumentNullException(String message, Throwable cause) {
        super(message, cause);
    }
}

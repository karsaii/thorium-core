package com.github.karsaii.core.exceptions;

import static com.github.karsaii.core.namespaces.wait.WaitExceptionFunctions.getSystemInformationMessage;

public class WrappedExecutionException extends RuntimeException {
    public WrappedExecutionException(String message) {
        super(message + getSystemInformationMessage());
    }

    public WrappedExecutionException(String message, Throwable cause) {
        super(message + getSystemInformationMessage(), cause);
    }
}

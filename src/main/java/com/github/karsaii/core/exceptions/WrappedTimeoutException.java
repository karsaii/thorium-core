package com.github.karsaii.core.exceptions;

import static com.github.karsaii.core.namespaces.wait.WaitExceptionFunctions.getSystemInformationMessage;

public class WrappedTimeoutException extends RuntimeException {
    public WrappedTimeoutException(String message) {
        super(message + getSystemInformationMessage());
    }

    public WrappedTimeoutException(String message, Throwable cause) {
        super(message + getSystemInformationMessage(), cause);
    }
}

package com.github.karsaii.core.exceptions;

import static com.github.karsaii.core.namespaces.wait.WaitExceptionFunctions.getSystemInformationMessage;

public class WrappedThreadInterruptedException extends RuntimeException {
    public WrappedThreadInterruptedException(String message) {
        super(message + getSystemInformationMessage());
    }

    public WrappedThreadInterruptedException(String message, Throwable cause) {
        super(message + getSystemInformationMessage(), cause);
    }
}

package com.github.karsaii.core.exceptions;

import static com.github.karsaii.core.namespaces.wait.WaitExceptionFunctions.getSystemInformationMessage;

public class WaitTimeoutException extends RuntimeException {
    public WaitTimeoutException(String message) {
        super(message + getSystemInformationMessage());
    }
}

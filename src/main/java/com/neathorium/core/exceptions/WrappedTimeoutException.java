package com.neathorium.core.exceptions;

import static com.neathorium.core.namespaces.wait.WaitExceptionFunctions.getSystemInformationMessage;

public class WrappedTimeoutException extends RuntimeException {
    public WrappedTimeoutException(String message) {
        super(message + getSystemInformationMessage());
    }

    public WrappedTimeoutException(String message, Throwable cause) {
        super(message + getSystemInformationMessage(), cause);
    }
}

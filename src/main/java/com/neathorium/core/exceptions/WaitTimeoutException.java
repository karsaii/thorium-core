package com.neathorium.core.exceptions;

import static com.neathorium.core.namespaces.wait.WaitExceptionFunctions.getSystemInformationMessage;

public class WaitTimeoutException extends RuntimeException {
    public WaitTimeoutException(String message) {
        super(message + getSystemInformationMessage());
    }

    public WaitTimeoutException(String message, Throwable cause) {
        super(message + getSystemInformationMessage(), cause);
    }
}

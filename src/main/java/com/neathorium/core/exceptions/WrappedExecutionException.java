package com.neathorium.core.exceptions;

import com.neathorium.core.namespaces.wait.WaitExceptionFunctions;

public class WrappedExecutionException extends RuntimeException {
    public WrappedExecutionException(String message) {
        super(message + WaitExceptionFunctions.getSystemInformationMessage());
    }

    public WrappedExecutionException(String message, Throwable cause) {
        super(message + WaitExceptionFunctions.getSystemInformationMessage(), cause);
    }
}

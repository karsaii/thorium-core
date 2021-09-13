package com.neathorium.core.exceptions;

import com.neathorium.core.namespaces.wait.WaitExceptionFunctions;

public class WrappedThreadInterruptedException extends RuntimeException {
    public WrappedThreadInterruptedException(String message) {
        super(message + WaitExceptionFunctions.getSystemInformationMessage());
    }

    public WrappedThreadInterruptedException(String message, Throwable cause) {
        super(message + WaitExceptionFunctions.getSystemInformationMessage(), cause);
    }
}

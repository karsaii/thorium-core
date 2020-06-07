package com.github.karsaii.core.exceptions;

public class MethodInvokeException extends RuntimeException {
    public MethodInvokeException() {
        super();
    }

    public MethodInvokeException(String s) {
        super(s);
    }

    public MethodInvokeException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public MethodInvokeException(Throwable throwable) {
        super(throwable);
    }
}

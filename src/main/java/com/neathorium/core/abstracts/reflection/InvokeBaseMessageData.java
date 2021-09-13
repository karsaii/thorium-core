package com.neathorium.core.abstracts.reflection;

import java.util.Objects;

public abstract class InvokeBaseMessageData {
    public final String message;
    public final String returnType;
    public final String parameterTypes;

    public InvokeBaseMessageData(String message, String returnType, String parameterTypes) {
        this.message = message;
        this.returnType = returnType;
        this.parameterTypes = parameterTypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var that = (InvokeBaseMessageData) o;
        return Objects.equals(message, that.message) && Objects.equals(returnType, that.returnType) && Objects.equals(parameterTypes, that.parameterTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, returnType, parameterTypes);
    }
}

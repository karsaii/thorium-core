package com.github.karsaii.core.records;

import com.github.karsaii.core.constants.validators.CoreFormatterConstants;

import java.util.Objects;

public class MethodMessageData {
    public final String nameof;
    public final String message;

    public MethodMessageData(String nameof, String message) {
        this.nameof = nameof;
        this.message = message;
    }

    public MethodMessageData(String message) {
        this(CoreFormatterConstants.EMPTY, message);
    }

    public String getMessage() {
        return nameof + ": " + message;
    }

    public String getMessage(String nameof) {
        return nameof + message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final var that = (MethodMessageData) o;
        return Objects.equals(nameof, that.nameof) && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameof, message);
    }

    @Override
    public String toString() {
        return getMessage();
    }
}

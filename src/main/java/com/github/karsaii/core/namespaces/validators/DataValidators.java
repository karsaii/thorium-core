package com.github.karsaii.core.namespaces.validators;

import com.github.karsaii.core.records.Data;

import static com.github.karsaii.core.namespaces.validators.CoreFormatter.getNamedErrorMessageOrEmpty;
import static com.github.karsaii.core.namespaces.validators.CoreFormatter.isFalseMessage;
import static org.apache.commons.lang3.StringUtils.isBlank;

public interface DataValidators {
    private static String isInvalidParametersMessage(Data<?> data) {
        final var baseName = "Data";
        return (
            CoreFormatter.isNullMessageWithName(data.exception, baseName + " Exception") +
            CoreFormatter.isNullMessageWithName(data.exceptionMessage, baseName + " Exception message") +
            MethodMessageDataValidators.isInvalidMessage(data.message)
        );
    }

    static String isInvalidMessage(Data<?> data) {
        final var baseName = "Data";
        var message = CoreFormatter.isNullMessageWithName(data, baseName);
        if (isBlank(message)) {
            message += isInvalidParametersMessage(data);
        }

        return getNamedErrorMessageOrEmpty("isInvalidMessage", message);
    }

    static String isInvalidOrFalseMessage(Data<?> data) {
        var message = isInvalidMessage(data);
        if (isBlank(message)) {
            message += isFalseMessage(data);
        }

        return getNamedErrorMessageOrEmpty("isInvalidOrFalseMessage", message);
    }
}

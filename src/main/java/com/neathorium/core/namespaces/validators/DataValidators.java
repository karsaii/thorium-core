package com.neathorium.core.namespaces.validators;

import com.neathorium.core.records.Data;

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

        return CoreFormatter.getNamedErrorMessageOrEmpty("isInvalidMessage", message);
    }

    static String isInvalidOrFalseMessage(Data<?> data) {
        var message = isInvalidMessage(data);
        if (isBlank(message)) {
            message += CoreFormatter.isFalseMessage(data);
        }

        return CoreFormatter.getNamedErrorMessageOrEmpty("isInvalidOrFalseMessage", message);
    }
}

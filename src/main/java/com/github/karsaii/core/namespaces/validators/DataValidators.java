package com.github.karsaii.core.namespaces.validators;

import com.github.karsaii.core.extensions.namespaces.CoreUtilities;
import com.github.karsaii.core.records.Data;
import com.github.karsaii.core.records.executor.ExecutionResultData;
import com.github.karsaii.core.constants.validators.CoreFormatterConstants;

import static com.github.karsaii.core.extensions.namespaces.NullableFunctions.isNotNull;
import static com.github.karsaii.core.namespaces.validators.CoreFormatter.isFalseMessage;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public interface DataValidators {
    static boolean isInitialized(Data<?> data) {
        return isNotNull(data);
    }

    static boolean isValid(Data<?> data) {
        return (
            isInitialized(data) &&
            isNotNull(data.exception) &&
            isNotBlank(data.exceptionMessage) &&
            MethodMessageDataValidators.isValid(data.message)
        );
    }

    static boolean isInvalid(Data<?> data) {
        return !isValid(data);
    }

    static boolean isValidNonFalse(Data<?> data) {
        return isValid(data) && CoreUtilities.isTrue(data.status);
    }

    static boolean isInvalidOrFalse(Data<?> data) {
        return !isValidNonFalse(data);
    }


    static boolean isExecutionValidNonFalse(Data<ExecutionResultData<Object>> data) {
        return isValid(data) && CoreUtilities.isTrue(data.status);
    }

    static boolean isExecutionInvalidOrFalse(Data<ExecutionResultData<Object>> data) {
        return !isValidNonFalse(data);
    }

    static boolean isFalse(Data<?> data) {
        return isNotNull(data) && CoreUtilities.isFalse(data.status);
    }

    static boolean isTrue(Data<?> data) {
        return isNotNull(data) && CoreUtilities.isTrue(data.status);
    }

    static boolean isValidAndFalse(Data<?> data) {
        return isValid(data) && CoreUtilities.isFalse(data.status);
    }

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

        return isNotBlank(message) ? "isInvalidMessage: " + CoreFormatterConstants.PARAMETER_ISSUES_LINE + message : CoreFormatterConstants.EMPTY;
    }

    static String isInvalidOrFalseMessage(Data<?> data) {
        var message = isInvalidMessage(data);
        if (isBlank(message)) {
            message += isFalseMessage(data);
        }

        return isNotBlank(message) ? "isInvalidOrFalseMessage: " + CoreFormatterConstants.PARAMETER_ISSUES_LINE + message : CoreFormatterConstants.EMPTY;
    }
}

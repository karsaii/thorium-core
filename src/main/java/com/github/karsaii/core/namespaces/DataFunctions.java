package com.github.karsaii.core.namespaces;

import com.github.karsaii.core.constants.validators.CoreFormatterConstants;
import com.github.karsaii.core.exceptions.ArgumentNullException;
import com.github.karsaii.core.extensions.namespaces.CoreUtilities;
import com.github.karsaii.core.extensions.namespaces.NullableFunctions;
import com.github.karsaii.core.namespaces.predicates.DataPredicates;
import com.github.karsaii.core.namespaces.validators.CoreFormatter;
import com.github.karsaii.core.records.Data;
import com.github.karsaii.core.records.MethodMessageData;

import java.util.Objects;

import static com.github.karsaii.core.extensions.namespaces.CoreUtilities.isException;
import static com.github.karsaii.core.extensions.namespaces.CoreUtilities.isNonException;
import static com.github.karsaii.core.extensions.namespaces.NullableFunctions.isNotNull;
import static com.github.karsaii.core.extensions.namespaces.NullableFunctions.isNull;
import static com.github.karsaii.core.namespaces.validators.CoreFormatter.isNullMessageWithName;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public interface DataFunctions {
    static String getNameIfAbsent(Data<?> data, String nameof) {
        var name = "";
        if (isNotNull(data)) {
            name = data.message.nameof;
            if (StringUtilities.uncontains(name, nameof)) {
                name = nameof + ": " + name;
            }
        } else {
            name = isNotBlank(nameof) ? nameof : "getNameIfAbsent";
        }

        return name;
    }

    static <T> String getMessageFromData(T object) {
        final var message = isNullMessageWithName(object, "Object");
        if (isNotBlank(message)) {
            return message;
        }

        if (!(object instanceof Data)) {
            return String.valueOf(object);
        }

        final var data = ((Data<?>) object);
        return data.message.formatter.apply(data.message.nameof, data.message.message);
    }

    static <T> String getStatusMessageFromData(T object) {
        final var errorMessage = isNullMessageWithName(object, "Object");
        if (isNotBlank(errorMessage)) {
            return errorMessage;
        }

        if (!(object instanceof Data)) {
            return String.valueOf(object);
        }

        final var data = (Data<?>) object;
        var message = data.message.toString();
        if (DataPredicates.isInvalidOrFalse(data) && CoreUtilities.isException(data.exception)) {
            message += "An exception has occurred: " + data.exception.getLocalizedMessage() + CoreFormatterConstants.END_LINE + data.exceptionMessage + CoreFormatterConstants.END_LINE;
        }

        return message;
    }

    private static void throwIfNullCore(String name, Data<?> data) {
        final var message = isNullMessageWithName(data, "Data parameter");
        if (isBlank(message)) {
            return;
        }

        final var nameof = isNotBlank(name) ? name : "throwIfNullCore";
        throw new ArgumentNullException(nameof + ": " + message);
    }

    private static void throwIfExceptionCore(String name, Data<?> data) {
        final var exception = data.exception;
        if (isNonException(exception)) {
            return;
        }

        final var nameof = isNotBlank(name) ? name : "throwIfExceptionCore";
        throw new RuntimeException(nameof + ": " + data.exceptionMessage, exception);
    }

    static void throwIfException(Data<?> data) {
        final var nameof = "throwIfException";
        throwIfNullCore(nameof, data);
        throwIfExceptionCore(nameof, data);
    }

    static void throwIfInvalid(Data<?> data) {
        final var nameof = "throwIfInvalid";
        throwIfNullCore(nameof, data);
        if (DataPredicates.isInvalidOrFalse(data)) {
            throwIfException(data);
            throw new IllegalStateException(nameof + ": " + data.message.toString());
        }
    }

    static <T> T getObject(Data<T> data) {
        return data.object;
    }

    static <T> boolean getStatus(Data<T> data) {
        return data.status;
    }

    static <T> MethodMessageData getMethodMessageData(Data<T> data) {
        return data.message;
    }
}

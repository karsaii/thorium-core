package com.github.karsaii.core.namespaces;

import com.github.karsaii.core.constants.CoreDataConstants;
import com.github.karsaii.core.constants.DataFactoryConstants;
import com.github.karsaii.core.extensions.DecoratedList;
import com.github.karsaii.core.records.Data;
import com.github.karsaii.core.records.MethodMessageData;
import com.github.karsaii.core.constants.validators.CoreFormatterConstants;
import com.github.karsaii.core.constants.CoreConstants;

import java.util.Objects;

import static com.github.karsaii.core.extensions.namespaces.CoreUtilities.areNotNull;
import static com.github.karsaii.core.extensions.namespaces.NullableFunctions.isNotNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public interface DataFactoryFunctions {
    static <T> Data<T> getWith(T object, boolean status, MethodMessageData messageData, Exception exception, String exceptionMessage) {
        final var isExceptionNull = Objects.isNull(exception);
        final var message = (isExceptionNull ? CoreFormatterConstants.EXCEPTION_WAS_NULL : CoreFormatterConstants.EMPTY) + messageData.message;
        final var exMessage = isNotBlank(exceptionMessage) ? exceptionMessage : ((isExceptionNull ? CoreFormatterConstants.EXCEPTION_WAS_NULL : exception.getMessage()));
        return new Data<T>(object, status, new MethodMessageData(messageData.nameof, message), exception, exMessage);
    }

    static <T> Data<T> getWithDefaultExceptionData(T object, boolean status, MethodMessageData messageData) {
        return getWith(object, status, messageData, DataFactoryConstants.DEFAULT_EXCEPTION, DataFactoryConstants.DEFAULT_EXCEPTION_MESSAGE);
    }

    static <T> Data<T> getWithSpecificObjectAndStatus(T object, boolean status) {
        return getWithDefaultExceptionData(object, status, DataFactoryConstants.DEFAULT_METHOD_MESSAGE_DATA);
    }

    static <T> Data<T> getWithNameAndDefaultExceptionData(T object, boolean status, String nameof) {
        return getWith(object, status, new MethodMessageData(nameof, ""),  DataFactoryConstants.DEFAULT_EXCEPTION, DataFactoryConstants.DEFAULT_EXCEPTION_MESSAGE);
    }

    static <T> Data<T> getWithMessageAndDefaultExceptionData(T object, boolean status, String message) {
        return getWith(object, status, new MethodMessageData("getWithMessageAndDefaultExceptionData", message), DataFactoryConstants.DEFAULT_EXCEPTION, DataFactoryConstants.DEFAULT_EXCEPTION_MESSAGE);
    }

    static Data<Void> getVoid() {
        return getWithSpecificObjectAndStatus(null, false);
    }

    static <T> Data<T> getWithMethodMessage(T object, boolean status, MethodMessageData messageData, Exception exception) {
        final var exceptionMessage = (Objects.isNull(exception) ? CoreFormatterConstants.EXCEPTION_WAS_NULL : exception.getMessage());
        return getWith(object, status, messageData, exception, exceptionMessage);
    }

    static <T> Data<T> getWithMethodMessage(T object, boolean status, MethodMessageData message) {
        return getWithDefaultExceptionData(object, status, message);
    }

    static <T> Data<T> getWithMethodMessage(T object, boolean status, String nameof, MethodMessageData messageData) {
        return getWithDefaultExceptionData(object, status, new MethodMessageData(nameof, messageData.message));
    }

    static <T> Data<T> getWithNameAndMessage(T object, boolean status, String nameof, String message, Exception exception, String exceptionMessage) {
        return getWith(object, status, new MethodMessageData(nameof, message), exception, exceptionMessage);
    }

    static <T> Data<T> getWithNameAndMessage(T object, boolean status, String nameof, MethodMessageData messageData, Exception exception, String exceptionMessage) {
        return getWith(object, status, new MethodMessageData(nameof, messageData.message), exception, exceptionMessage);
    }

    static <T> Data<T> getWithNameAndMessage(T object, boolean status, String nameof, String message, Exception exception) {
        return getWith(object, status, new MethodMessageData(nameof, message), exception, exception.getMessage());
    }

    static <T> Data<T> getWithNameAndMessage(T object, boolean status, String nameof, String message) {
        return getWithDefaultExceptionData(object, status, new MethodMessageData(nameof, message));
    }

    static <T> Data<T> getValidWithNameAndMessage(T object, String nameof, String message) {
        return getWithDefaultExceptionData(object, true, new MethodMessageData(nameof, message));
    }

    static <T> Data<T> getInvalidWithNameAndMessage(T object, String nameof, String message) {
        return getWithNameAndMessage(object, false, nameof, message);
    }

    static Data<Boolean> getInvalidBooleanWithNameAndMessage(String nameof, String message) {
        return getWithNameAndMessage(false, false, nameof, message);
    }

    static <T> Data<T> getWithMessage(T object, boolean status, String nameof, String message, Exception exception, String exceptionMessage) {
        return getWith(object, status, new MethodMessageData(nameof, message), exception, exceptionMessage);
    }

    static <T> Data<T> getWithMessageAndExceptionDefaults(T object, boolean status, String nameof, String message) {
        return getWith(object, status, new MethodMessageData(nameof, message), CoreConstants.EXCEPTION, CoreFormatterConstants.NON_EXCEPTION_MESSAGE);
    }

    static <T> Data<T> getWithMessage(T object, boolean status, String message, Exception exception, String exceptionMessage) {
        return getWithMessage(object, status, "getWithMessage", message, exception, exceptionMessage);
    }

    static <T> Data<T> getWithMessage(T object, boolean status, String message, Exception exception) {
        return getWithMessage(object, status, "getWithMessage", message, exception, exception.getMessage());
    }

    static <T> Data<T> getWithMessage(T object, boolean status, String message) {
        return getWithMessageAndExceptionDefaults(object, status, "getWithMessage", message);
    }

    static <T> Data<T> getErrorFunction(T object, String nameof, String message) {
        return getWithNameAndMessage(object, false, nameof, message);
    }

    static <T> Data<T> getErrorFunction(T object, String message) {
        return getErrorFunction(object, "getErrorFunction", message);
    }

    static <T> Data<T> replaceMethodMessageAndName(Data<T> data, String nameof, MethodMessageData messageData) {
        return getWith(data.object, data.status, new MethodMessageData(nameof, messageData.message), data.exception, data.exceptionMessage);
    }

    static <T> Data<T> replaceMethodMessageData(Data<T> data, MethodMessageData message) {
        return getWith(data.object, data.status, message, data.exception, data.exceptionMessage);
    }

    static <T> Data<T> replaceNameAndMessage(Data<T> data, String nameof, String message) {
        return getWith(data.object, data.status, new MethodMessageData(nameof, message), data.exception, data.exceptionMessage);
    }

    static <T> Data<T> replaceObject(Data<?> data, T object) {
        return getWith(object, data.status, data.message, data.exception, data.exceptionMessage);
    }

    static <T> Data<T> replaceStatus(Data<T> data, boolean status) {
        return getWith(data.object, status, data.message, data.exception, data.exceptionMessage);
    }

    static <T> Data<T> replaceMessage(Data<T> data, String message) {
        return getWith(data.object, data.status, new MethodMessageData(message), data.exception, data.exceptionMessage);
    }

    static <T> Data<T> replaceStatusAndMessage(Data<T> data, boolean status, String message) {
        return getWith(data.object, status, new MethodMessageData(data.message.nameof, message), data.exception, data.exceptionMessage);
    }

    static <T> Data<T> replaceName(Data<T> data, String nameof) {
        return getWithMessage(data.object, data.status, data.message.getMessage(nameof), data.exception, data.exceptionMessage);
    }

    static <T> Data<T> prependMessage(Data<T> data, String message) {
        return getWithMessage(data.object, data.status, message + data.message, data.exception, data.exceptionMessage);
    }

    static <T> Data<T> prependMessage(Data<T> data, String nameof, String message) {
        return getWith(data.object, data.status, new MethodMessageData(nameof, message + data.message.message), data.exception, data.exceptionMessage);
    }

    static <T> Data<T> replaceMessage(Data<T> data, String nameof, String message) {
        return getWith(data.object, data.status, new MethodMessageData(nameof, message), data.exception, data.exceptionMessage);
    }

    static <T> Data<T> appendMessage(Data<T> data, String message) {
        return getWithMessage(data.object, data.status, data.message + message, data.exception, data.exceptionMessage);
    }

    static <T> Data<T> appendMessage(Data<T> data, String nameof, String message) {
        return getWith(data.object, data.status, new MethodMessageData(nameof, data.message.message + message), data.exception, data.exceptionMessage);
    }

    static Data<Boolean> getBoolean(boolean status, String nameof, String message, Exception exception, String exceptionMessage) {
        return areNotNull(message, nameof, exception, exceptionMessage) && status ? (
            getWithMessage(status, status, nameof, message, exception, exceptionMessage)
        ) : CoreDataConstants.NULL_BOOLEAN;
    }

    static Data<Boolean> getBoolean(boolean status, String message, Exception exception, String exceptionMessage) {
        return getWithMessage(status, status, message, exception, exceptionMessage);
    }

    static Data<Boolean> getBoolean(boolean status, String nameof, String message, Exception exception) {
        return getWithMethodMessage(status, status, new MethodMessageData(nameof, message), exception);
    }

    static Data<Boolean> getBoolean(boolean status, String message, Exception exception) {
        return getWithMethodMessage(status, status, new MethodMessageData(message), exception);
    }

    static Data<Boolean> getBoolean(boolean status, String nameof, String message) {
        return getWithMethodMessage(status, status, new MethodMessageData(nameof, message));
    }

    static Data<Boolean> getBoolean(boolean status, String message) {
        return getWithMethodMessage(status, status, new MethodMessageData(message));
    }

    static Data<String> getString(String object, String message) {
        return getWithMessage(object, isNotBlank(object), message);
    }

    static Data<String> getString(String object, String nameof, String message) {
        return getWithMethodMessage(object, isNotBlank(object), new MethodMessageData(nameof, message));
    }

    static Data<String> getString(String object, String message, Exception exception, String exceptionMessage) {
        return getWithMessage(object, isNotBlank(object), message, exception, exceptionMessage);
    }

    static Data<Object[]> getArrayWithName(Object[] object, boolean status, String nameof) {
        return areNotNull(object, nameof) ? (
            getWithMethodMessage(object, status, new MethodMessageData(nameof, "Element was found. Array of length " + object.length + " was constructed" + CoreFormatterConstants.END_LINE))
        ) : CoreDataConstants.NULL_PARAMETER_ARRAY;
    }

    static Data<Object[]> getArrayWithName(Object[] object) {
        return isNotNull(object) ? (
            getWithMethodMessage(object, true, new MethodMessageData("getArrayWithName", "Element was found. Array of length " + object.length + " was constructed" + CoreFormatterConstants.END_LINE))
        ) : CoreDataConstants.NULL_PARAMETER_ARRAY;
    }
}

package com.github.karsaii.core.namespaces;

import com.github.karsaii.core.records.Data;
import com.github.karsaii.core.records.MethodMessageData;

import static com.github.karsaii.core.extensions.namespaces.CoreUtilities.isException;
import static com.github.karsaii.core.extensions.namespaces.NullableFunctions.isNotNull;
import static com.github.karsaii.core.extensions.namespaces.NullableFunctions.isNull;
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
        return (object instanceof Data) ? ((Data<?>) object).message.getMessage() : String.valueOf(object);
    }

    static void throwIfException(Data<?> data) throws Exception {
        if (isNull(data)) {
            return;
        }

        final var exception = data.exception;
        if (isException(exception)) {
            throw exception;
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

package com.github.karsaii.core.namespaces.factories;

import com.github.karsaii.core.extensions.namespaces.NullableFunctions;
import com.github.karsaii.core.namespaces.validators.CoreFormatter;
import com.github.karsaii.core.records.MethodMessageData;

import java.util.function.BiFunction;

import static com.github.karsaii.core.namespaces.validators.CoreFormatter.isNullMessageWithName;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public interface MethodMessageDataFactory {
    static MethodMessageData getWith(BiFunction<String, String, String> formatter, String nameof, String message) {
        final var errorMessage = isNullMessageWithName(formatter, "Formatter function");
        if (NullableFunctions.isNull(formatter)) {
            throw new IllegalArgumentException(errorMessage);
        }

        final var lNameof = isNotBlank(nameof) ? nameof : "getWith";
        final var lMessage = isNotBlank(message) ? message : "Default method message";
        return new MethodMessageData(formatter, lNameof, lMessage);
    }

    static MethodMessageData getWith(String nameof, String message) {
        return getWith(CoreFormatter::getMethodMessageDataFormatted, nameof, message);
    }

    static MethodMessageData getWithMessage(String message) {
        return getWith("getWith", message);
    }
}

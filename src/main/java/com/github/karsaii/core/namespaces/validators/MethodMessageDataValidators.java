package com.github.karsaii.core.namespaces.validators;

import com.github.karsaii.core.records.MethodMessageData;

import static com.github.karsaii.core.namespaces.validators.CoreFormatter.getNamedErrorMessageOrEmpty;
import static com.github.karsaii.core.namespaces.validators.CoreFormatter.isBlankMessageWithName;
import static com.github.karsaii.core.namespaces.validators.CoreFormatter.isNullMessageWithName;
import static org.apache.commons.lang3.StringUtils.isBlank;

public interface MethodMessageDataValidators {

    static String isInvalidMessage(MethodMessageData data) {
        final var baseName = "Data";
        var message = isNullMessageWithName(data, baseName);
        if (isBlank(message)) {
            message += (
                isBlankMessageWithName(data.message, baseName + " Message") +
                isBlankMessageWithName(data.nameof, baseName + " Name of")
            );
        }

        return  getNamedErrorMessageOrEmpty("isInvalidMessage: ", message);
    }
}

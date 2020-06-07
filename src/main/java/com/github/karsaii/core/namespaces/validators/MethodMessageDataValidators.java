package com.github.karsaii.core.namespaces.validators;

import com.github.karsaii.core.records.MethodMessageData;
import com.github.karsaii.core.constants.validators.CoreFormatterConstants;

import static com.github.karsaii.core.extensions.namespaces.NullableFunctions.isNotNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public interface MethodMessageDataValidators {
    static boolean isValid(MethodMessageData data) {
        return isNotNull(data) && isNotBlank(data.message) && isNotNull(data.nameof);
    }

    static String isInvalidMessage(MethodMessageData data) {
        final var baseName = "Data";
        var message = CoreFormatter.isNullMessageWithName(data, baseName);
        if (isBlank(message)) {
            message += CoreFormatter.isBlankMessageWithName(data.message, baseName + " Message");
        }

        final var nameParameterDescriptor = baseName + " Name of source";
        message += isNotBlank(message) ? CoreFormatter.isBlankMessageWithName(data.nameof, nameParameterDescriptor) : CoreFormatter.isNullMessageWithName(data.nameof, nameParameterDescriptor);

        return isNotBlank(message) ? "isInvalidMessage: " + CoreFormatterConstants.PARAMETER_ISSUES_LINE + message : CoreFormatterConstants.EMPTY;
    }
}

package com.github.karsaii.core.namespaces.validators;

import com.github.karsaii.core.records.HandleResultData;

import static com.github.karsaii.core.namespaces.validators.CoreFormatter.getNamedErrorMessageOrEmpty;
import static com.github.karsaii.core.namespaces.validators.CoreFormatter.isNullMessageWithName;
import static org.apache.commons.lang3.StringUtils.isBlank;

public interface HandlerResultDataValidator {
    static <T, U> String isInvalidHandlerResultDataMessage(HandleResultData<T, U> data) {
        final var baseName = "Handle Result Data";
        var message = isNullMessageWithName(data, baseName);
        if (isBlank(message)) {
            message += (
                isNullMessageWithName(data.caster, baseName + " Caster") +
                isNullMessageWithName(data.parameter, baseName + " Parameter")
            );
        }

        return getNamedErrorMessageOrEmpty("isInvalidHandlerResultDataMessage", message);
    }
}

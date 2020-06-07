package com.github.karsaii.core.namespaces.validators;

import com.github.karsaii.core.records.HandleResultData;
import com.github.karsaii.core.constants.validators.CoreFormatterConstants;

import static com.github.karsaii.core.namespaces.validators.CoreFormatter.isNullMessageWithName;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

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

        return isNotBlank(message) ? "isInvalidHandlerResultDataMessage: " + CoreFormatterConstants.PARAMETER_ISSUES_LINE + message : CoreFormatterConstants.EMPTY;
    }
}

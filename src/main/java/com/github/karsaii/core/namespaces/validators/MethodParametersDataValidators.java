package com.github.karsaii.core.namespaces.validators;

import com.github.karsaii.core.records.MethodParametersData;
import com.github.karsaii.core.constants.validators.CoreFormatterConstants;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public interface MethodParametersDataValidators {
    static String isValid(MethodParametersData data) {
        var message = CoreFormatter.isNullMessageWithName(data, "Method Parameter data");
        if (isBlank(message)) {
            message += (
                CoreFormatter.isNullMessageWithName(data.methodName, "Method name") +
                CoreFormatter.isNullMessageWithName(data.validator, "Validator")
            );
        }

        return isNotBlank(message) ? CoreFormatterConstants.PARAMETER_ISSUES_LINE + message : CoreFormatterConstants.EMPTY;
    }
}

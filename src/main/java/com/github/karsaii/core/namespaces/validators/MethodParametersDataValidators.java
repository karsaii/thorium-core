package com.github.karsaii.core.namespaces.validators;

import com.github.karsaii.core.records.MethodParametersData;

import static com.github.karsaii.core.namespaces.validators.CoreFormatter.getNamedErrorMessageOrEmpty;
import static org.apache.commons.lang3.StringUtils.isBlank;

public interface MethodParametersDataValidators {
    static String isValid(MethodParametersData data) {
        var message = CoreFormatter.isNullMessageWithName(data, "Method Parameter data");
        if (isBlank(message)) {
            message += (
                CoreFormatter.isNullMessageWithName(data.methodName, "Method name") +
                CoreFormatter.isNullMessageWithName(data.validator, "Validator")
            );
        }

        return getNamedErrorMessageOrEmpty("isValid", message);
    }
}

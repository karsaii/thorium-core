package com.github.karsaii.core.namespaces.validators;

import com.github.karsaii.core.records.MethodParametersData;
import com.github.karsaii.core.records.MethodSourceData;
import com.github.karsaii.core.constants.validators.CoreFormatterConstants;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public interface MethodRepositoryValidators {
    static String validateMethodSourceData(MethodSourceData data) {
        var message = CoreFormatter.isNullMessageWithName(data, "Method Get Parameters data");
        if (isBlank(message)) {
            message +=  (
                CoreFormatter.isNullMessageWithName(data.methodMap, "Method map") +
                CoreFormatter.isNullMessageWithName(data.list, "Method List") +
                CoreFormatter.isFalseMessageWithName(data.defaultValue, "Default Value")
            );
        }
        return isNotBlank(message) ? "validateMethodGetCommonParametersData: " + CoreFormatterConstants.PARAMETER_ISSUES_LINE + message : CoreFormatterConstants.EMPTY;
    }

    static String validateMethodParametersData(MethodParametersData parameterData) {
        var message = CoreFormatter.isNullMessageWithName(parameterData, "Method parameters data");
        if (isBlank(message)) {
            message +=  (
                CoreFormatter.isNullMessageWithName(parameterData.validator, "Condition method") +
                CoreFormatter.isBlankMessageWithName(parameterData.methodName, "Method name")
            );
        }

        return isNotBlank(message) ? "validateMethodParametersData: " + CoreFormatterConstants.PARAMETER_ISSUES_LINE + message : CoreFormatterConstants.EMPTY;
    }


    static String validateGetMethodFromList(MethodSourceData data, MethodParametersData parameterData) {
        return validateMethodSourceData(data) + validateMethodParametersData(parameterData);
    }
}

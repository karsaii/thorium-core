package com.github.karsaii.core.namespaces.validators.process;

import com.github.karsaii.core.extensions.namespaces.predicates.BasicPredicates;
import com.github.karsaii.core.extensions.namespaces.validators.FileUtilitiesValidators;
import com.github.karsaii.core.records.process.ApplicationData;

import static com.github.karsaii.core.constants.process.ProcessFunctionsValidatorsConstants.APPLICATION_NAME;
import static com.github.karsaii.core.constants.process.ProcessFunctionsValidatorsConstants.ARGUMENTS;
import static com.github.karsaii.core.constants.process.ProcessFunctionsValidatorsConstants.PATH;
import static com.github.karsaii.core.namespaces.validators.CoreFormatter.getNamedErrorMessageOrEmpty;
import static com.github.karsaii.core.namespaces.validators.CoreFormatter.isBlankMessageWithName;
import static com.github.karsaii.core.namespaces.validators.CoreFormatter.isNullMessageWithName;
import static org.apache.commons.lang3.StringUtils.isBlank;

public interface ProcessFunctionsValidators {
    static String isValidGetBuilderParameters(ApplicationData data) {
        var message = isNullMessageWithName(data, "Application Data");
        if (isBlank(message)) {
            message += (
                isBlankMessageWithName(data.name, APPLICATION_NAME) +
                isBlankMessageWithName(data.path, PATH) +
                isNullMessageWithName(data.arguments, ARGUMENTS)
            );
        }

        if (isBlank(message)) {
            final var arguments = data.arguments;
            message += FileUtilitiesValidators.isExistingMessage(data.path);
            if (BasicPredicates.isPositiveNonZero(arguments.length())) {
                message += isBlankMessageWithName(arguments, ARGUMENTS);
            }
        }

        return getNamedErrorMessageOrEmpty("isValidGetBuilderParameters", message);
    }
}

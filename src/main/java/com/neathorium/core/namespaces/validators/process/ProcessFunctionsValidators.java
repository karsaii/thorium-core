package com.neathorium.core.namespaces.validators.process;

import com.neathorium.core.extensions.namespaces.predicates.BasicPredicates;
import com.neathorium.core.extensions.namespaces.validators.FileUtilitiesValidators;
import com.neathorium.core.records.process.ApplicationData;
import com.neathorium.core.constants.process.ProcessFunctionsValidatorsConstants;
import com.neathorium.core.namespaces.validators.CoreFormatter;

import static org.apache.commons.lang3.StringUtils.isBlank;

public interface ProcessFunctionsValidators {
    static String isValidGetBuilderParameters(ApplicationData data) {
        var message = CoreFormatter.isNullMessageWithName(data, "Application Data");
        if (isBlank(message)) {
            message += (
                CoreFormatter.isBlankMessageWithName(data.name, ProcessFunctionsValidatorsConstants.APPLICATION_NAME) +
                CoreFormatter.isBlankMessageWithName(data.path, ProcessFunctionsValidatorsConstants.PATH) +
                CoreFormatter.isNullMessageWithName(data.arguments, ProcessFunctionsValidatorsConstants.ARGUMENTS)
            );
        }

        if (isBlank(message)) {
            final var arguments = data.arguments;
            message += FileUtilitiesValidators.isExistingMessage(data.path);
            if (BasicPredicates.isPositiveNonZero(arguments.length())) {
                message += CoreFormatter.isBlankMessageWithName(arguments, ProcessFunctionsValidatorsConstants.ARGUMENTS);
            }
        }

        return CoreFormatter.getNamedErrorMessageOrEmpty("isValidGetBuilderParameters", message);
    }
}

package com.github.karsaii.core.namespaces.process;

import com.github.karsaii.core.constants.process.ProcessConstants;
import com.github.karsaii.core.constants.systemidentity.BasicSystemIdentityConstants;
import com.github.karsaii.core.namespaces.DataFactoryFunctions;
import com.github.karsaii.core.namespaces.predicates.DataPredicates;
import com.github.karsaii.core.namespaces.validators.process.ProcessFunctionsValidators;
import com.github.karsaii.core.records.Data;
import com.github.karsaii.core.namespaces.formatter.process.ProcessFunctionsFormatter;
import com.github.karsaii.core.records.process.ApplicationData;

import java.io.File;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public interface ProcessFunctions {
    static String getPlatformOrDefault(String os, String defaultOs) {
        if (isNotBlank(os)) {
            return os.contains(BasicSystemIdentityConstants.WINDOWS) ? BasicSystemIdentityConstants.WINDOWS : BasicSystemIdentityConstants.UNKNOWN;
        }

        return isNotBlank(defaultOs) ? defaultOs : BasicSystemIdentityConstants.UNKNOWN;
    }

    static String getPlatform(String os) {
        return getPlatformOrDefault(os, BasicSystemIdentityConstants.UNKNOWN);
    }

    static File getNullFile(String platform) {
        final var nullFile = ProcessConstants.NULL_FILE_PLATFORM_MAP.getOrDefault(platform, ProcessConstants.ANY_NULL_FILE_PATH);
        return new File(nullFile);
    }

    static Data<ProcessBuilder> getBuilder(ApplicationData data) {
        final var nameof = "getBuilder";
        final var errorMessage = ProcessFunctionsValidators.isValidGetBuilderParameters(data);
        if (isNotBlank(errorMessage)) {
            return DataFactoryFunctions.getInvalidWithNameAndMessage(ProcessConstants.NULL_BUILDER, nameof, errorMessage);
        }

        final var path = data.path;
        final var arguments = data.arguments;
        final var builder = new ProcessBuilder().command(path, arguments);
        final var message = ProcessFunctionsFormatter.getBuilderFormattedParametersMessage(data.name, path, arguments);

        return DataFactoryFunctions.getWithNameAndMessage(builder, true, nameof, message);
    }

    static Data<ProcessBuilder> getBuilderWithRedirects(ApplicationData data, String os) {
        final var nameof = "getBuilderWithRedirects";
        final var errorMessage = ProcessFunctionsValidators.isValidGetBuilderParameters(data);
        if (isNotBlank(errorMessage)) {
            return DataFactoryFunctions.getInvalidWithNameAndMessage(ProcessConstants.NULL_BUILDER, nameof, errorMessage);
        }

        final var builderData = getBuilder(data);
        if (DataPredicates.isInvalidOrFalse(builderData)) {
            return builderData;
        }

        final var builder = builderData.object;
        builder.redirectOutput(getNullFile(getPlatform(os))).redirectErrorStream(true);

        return DataFactoryFunctions.replaceObject(builderData, builder);
    }
}

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public interface ProcessFunctions {
    private static List<String> getCommandList(String path, String arguments) {
        final var commandList = new ArrayList<String>();
        commandList.add(path);
        commandList.addAll(Arrays.asList(arguments.split(" ")));

        return commandList;
    }

    static String getPlatformOrDefault(String os, String defaultOs) {
        if (isNotBlank(os)) {
            return os.contains(BasicSystemIdentityConstants.WINDOWS) ? BasicSystemIdentityConstants.WINDOWS : BasicSystemIdentityConstants.UNKNOWN;
        }

        return isNotBlank(defaultOs) ? defaultOs : BasicSystemIdentityConstants.UNKNOWN;
    }

    static String getPlatform(String os) {
        return getPlatformOrDefault(os, BasicSystemIdentityConstants.UNKNOWN);
    }

    static File getNullFile(Map<String, String> map, String defaultNullPath, String platform) {
        final var nullFile = map.getOrDefault(platform, defaultNullPath);
        return new File(nullFile);
    }

    static File getNullFile(String platform) {
        return getNullFile(ProcessConstants.NULL_FILE_PLATFORM_MAP, ProcessConstants.ANY_NULL_FILE_PATH, platform);
    }

    static Data<ProcessBuilder> getBuilder(ApplicationData data) {
        final var nameof = "getBuilder";
        final var errorMessage = ProcessFunctionsValidators.isValidGetBuilderParameters(data);
        if (isNotBlank(errorMessage)) {
            return DataFactoryFunctions.getInvalidWith(ProcessConstants.NULL_BUILDER, nameof, errorMessage);
        }

        final var path = data.path;
        final var arguments = data.arguments;
        final var builder = new ProcessBuilder().command(getCommandList(path, arguments));
        final var message = ProcessFunctionsFormatter.getBuilderFormattedParametersMessage(data);

        return DataFactoryFunctions.getWith(builder, true, nameof, message);
    }

    static Data<ProcessBuilder> getBuilderWithRedirects(ApplicationData data, String os) {
        final var nameof = "getBuilderWithRedirects";
        final var errorMessage = ProcessFunctionsValidators.isValidGetBuilderParameters(data);
        if (isNotBlank(errorMessage)) {
            return DataFactoryFunctions.getInvalidWith(ProcessConstants.NULL_BUILDER, nameof, errorMessage);
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

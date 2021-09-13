package com.neathorium.core.namespaces.process;

import com.neathorium.core.constants.process.ProcessConstants;
import com.neathorium.core.namespaces.DataFactoryFunctions;
import com.neathorium.core.namespaces.predicates.DataPredicates;
import com.neathorium.core.namespaces.systemidentity.BasicSystemIdentityFunctions;
import com.neathorium.core.namespaces.validators.process.ProcessFunctionsValidators;
import com.neathorium.core.platform.enums.PlatformKey;
import com.neathorium.core.records.Data;
import com.neathorium.core.namespaces.formatter.process.ProcessFunctionsFormatter;
import com.neathorium.core.records.process.ApplicationData;

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

    static File getNullFile(Map<PlatformKey, String> map, String defaultNullPath, PlatformKey platform) {
        final var nullFile = map.getOrDefault(platform, defaultNullPath);
        return new File(nullFile);
    }

    static File getNullFile(PlatformKey platform) {
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
        builder.redirectOutput(getNullFile(BasicSystemIdentityFunctions.getSystemTypeOrUnknown(os))).redirectErrorStream(true);

        return DataFactoryFunctions.replaceObject(builderData, builder);
    }
}

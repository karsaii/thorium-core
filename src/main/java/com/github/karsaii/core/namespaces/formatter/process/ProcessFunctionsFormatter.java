package com.github.karsaii.core.namespaces.formatter.process;

import com.github.karsaii.core.constants.validators.CoreFormatterConstants;

public interface ProcessFunctionsFormatter {
    static String getBuilderFormattedParametersMessage(String name, String path, String arguments) {
        final var nameFragment = name + " starting" + CoreFormatterConstants.COLON_NEWLINE;
        final var pathFragment = "Path: " + path + CoreFormatterConstants.COLON_NEWLINE;
        final var argumentFragment = "Arguments: " + arguments + CoreFormatterConstants.END_LINE;
        return nameFragment + pathFragment + argumentFragment;
    }
}
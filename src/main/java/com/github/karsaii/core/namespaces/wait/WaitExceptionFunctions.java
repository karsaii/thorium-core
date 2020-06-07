package com.github.karsaii.core.namespaces.wait;

import com.github.karsaii.core.constants.SystemIdentityConstants;
import com.github.karsaii.core.constants.validators.CoreFormatterConstants;

public interface WaitExceptionFunctions {
    static String getSystemInformationMessage() {
        return (
            CoreFormatterConstants.NEW_LINE +
            "System info: host: \"" +
            SystemIdentityConstants.HOST_NAME +
            "\", ip: \"" +
            SystemIdentityConstants.HOST_ADDRESS +
            "\", os.name: \"" +
            System.getProperty("os.name") +
            ", os.arch: \"" +
            System.getProperty("os.arch") +
            "\", os.version: \"" +
            System.getProperty("os.version") +
            "\", java.version: \"" +
            System.getProperty("java.version") +
            "\"."
        );
    }
}

package com.neathorium.core.namespaces.wait;

import com.neathorium.core.constants.systemidentity.ConcreteSystemIdentityConstants;
import com.neathorium.core.constants.validators.CoreFormatterConstants;

public interface WaitExceptionFunctions {
    static String getSystemInformationMessage() {
        return (
            CoreFormatterConstants.NEW_LINE +
            "System info: host: \"" +
            ConcreteSystemIdentityConstants.HOST_NAME +
            "\", ip: \"" +
            ConcreteSystemIdentityConstants.HOST_ADDRESS +
            "\", os.name: \"" +
            System.getProperty("os.name") +
            "\", os.arch: \"" +
            System.getProperty("os.arch") +
            "\", os.version: \"" +
            System.getProperty("os.version") +
            "\", java.version: \"" +
            System.getProperty("java.version") +
            "\"."
        );
    }
}

package com.github.karsaii.core.namespaces.systemidentity;

import com.github.karsaii.core.constants.systemidentity.BasicSystemIdentityConstants;
import com.github.karsaii.core.constants.validators.CoreFormatterConstants;
import com.github.karsaii.core.namespaces.StringUtilities;

import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public interface BasicSystemIdentityFunctions {
    static boolean isLinux() {
        return StringUtilities.contains(BasicSystemIdentityConstants.OS_NAME, BasicSystemIdentityConstants.LINUX);
    }

    static boolean isMac() {
        return StringUtilities.contains(BasicSystemIdentityConstants.OS_NAME, BasicSystemIdentityConstants.MAC);
    }

    static boolean isWindows() {
        return StringUtilities.contains(BasicSystemIdentityConstants.OS_NAME, BasicSystemIdentityConstants.WINDOWS);
    }

    private static String isOrUnknown(boolean status, String identity) {
        return status ? identity : BasicSystemIdentityConstants.UNKNOWN;
    }

    static String isWindowsOrUnknown() {
        return isOrUnknown(isWindows(), BasicSystemIdentityConstants.WINDOWS);
    }

    static String isLinuxOrUnknown() {
        return isOrUnknown(isLinux(), BasicSystemIdentityConstants.LINUX);
    }

    static String isMacOrUnknown() {
        return isOrUnknown(isMac(), BasicSystemIdentityConstants.MAC);
    }

    static String getSystemTypeOrUnknown() {
        if (isWindows()) {
            return BasicSystemIdentityConstants.WINDOWS;
        }

        if (isMac()) {
            return BasicSystemIdentityConstants.MAC;
        }

        if (isLinux()) {
            return BasicSystemIdentityConstants.LINUX;
        }

        return BasicSystemIdentityConstants.UNKNOWN;
    }

    static String isSystemType(String os) {
        final var osType = getSystemTypeOrUnknown();

        final var isUnknown = Objects.equals(osType, BasicSystemIdentityConstants.UNKNOWN);
        var message = "";
        if (isUnknown) {
            message += "Operating System is (\"" + osType + "\"), which is unsupported" + CoreFormatterConstants.END_LINE;
        }

        final var isExpectedUnsupported = StringUtilities.uncontains(os, osType);
        if (!isUnknown && isExpectedUnsupported) {
            message += "Operating system is (\"" + osType + "\"), expected is (\"" + os + "\"), is unsupported";
        }

        if (isNotBlank(message)) {
            throw new IllegalStateException("There is an issue: " + message);
        }

        return osType;
    }
}

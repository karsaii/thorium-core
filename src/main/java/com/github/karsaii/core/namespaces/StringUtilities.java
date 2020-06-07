package com.github.karsaii.core.namespaces;

import org.apache.commons.lang3.StringUtils;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public interface StringUtilities {
    private static boolean parameterGuard(String object, String expected) {
        return isNotBlank(object) && isNotEmpty(expected);
    }

    static boolean contains(String object, String expected) {
        return parameterGuard(object, expected) && StringUtils.contains(object, expected);
    }

    static boolean startsWithCaseSensitive(String object, String expected) {
        return parameterGuard(object, expected) && StringUtils.startsWith(object, expected);
    }

    static boolean startsWithCaseInsensitive(String object, String expected) {
        return parameterGuard(object, expected) && StringUtils.startsWithIgnoreCase(object, expected);
    }

    static boolean endsWithCaseSensitive(String object, String expected) {
        return parameterGuard(object, expected) && StringUtils.endsWith(object, expected);
    }

    static boolean endsWithCaseInsensitive(String object, String expected) {
        return parameterGuard(object, expected) && StringUtils.endsWithIgnoreCase(object, expected);
    }

    static boolean uncontains(String object, String expected) {
        return parameterGuard(object, expected) && !contains(object, expected);
    }

    static boolean startsNotWithCaseSensitive(String object, String expected) {
        return parameterGuard(object, expected) && !startsWithCaseSensitive(object, expected);
    }

    static boolean startsNotWithCaseInsensitive(String object, String expected) {
        return parameterGuard(object, expected) && !startsWithCaseInsensitive(object, expected);
    }

    static boolean endsNotWithCaseSensitive(String object, String expected) {
        return parameterGuard(object, expected) && !endsWithCaseSensitive(object, expected);
    }

    static boolean endsNotWithCaseInsensitive(String object, String expected) {
        return parameterGuard(object, expected) &&  !endsWithCaseInsensitive(object, expected);
    }
}

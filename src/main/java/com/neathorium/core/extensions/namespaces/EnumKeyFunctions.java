package com.neathorium.core.extensions.namespaces;

import com.neathorium.core.extensions.interfaces.IEnumKey;
import com.neathorium.core.namespaces.StringUtilities;

import static com.neathorium.core.namespaces.validators.CoreFormatter.isBlankMessageWithName;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public interface EnumKeyFunctions {
    static <T extends IEnumKey> boolean isIn(String value, T key) {
        return CoreUtilities.areAny(StringUtilities.contains(value), key.getNames());
    }

    static <T extends IEnumKey> boolean isNotIn(String value, T key) {
        return CoreUtilities.areAll(StringUtilities.uncontains(value), key.getNames());
    }

    static <T extends IEnumKey> T getKey(String value, String valueName, T defaultValue, T[] values) {
        final var errorMessage = isBlankMessageWithName(value, valueName);
        if (isNotBlank(errorMessage)) {
            return defaultValue;
        }

        final var trimmedValue = value.trim();
        for (var val : values) {
            if (isNotIn(trimmedValue, val)) {
                continue;
            }

            return val;
        }

        return defaultValue;
    }
}

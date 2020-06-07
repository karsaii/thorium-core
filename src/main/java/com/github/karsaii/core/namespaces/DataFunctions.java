package com.github.karsaii.core.namespaces;

import com.github.karsaii.core.extensions.namespaces.BasicPredicateFunctions;
import com.github.karsaii.core.extensions.namespaces.CoreUtilities;
import com.github.karsaii.core.records.Data;
import org.apache.commons.lang3.ArrayUtils;
import com.github.karsaii.core.constants.CoreConstants;

import static com.github.karsaii.core.extensions.namespaces.CoreUtilities.isException;
import static com.github.karsaii.core.extensions.namespaces.NullableFunctions.isNotNull;
import static com.github.karsaii.core.extensions.namespaces.NullableFunctions.isNull;
import static com.github.karsaii.core.namespaces.validators.DataValidators.isValid;
import static com.github.karsaii.core.namespaces.validators.DataValidators.isValidNonFalse;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public interface DataFunctions {
    static String getNameIfAbsent(Data<?> data, String nameof) {
        var name = "";
        if (isNotNull(data)) {
            name = data.message.nameof;
            if (StringUtilities.uncontains(name, nameof)) {
                name = nameof + ": " + name;
            }
        } else {
            name = isNotBlank(nameof) ? nameof : "getNameIfAbsent";
        }

        return name;
    }

    static boolean isFalse(Data<?> data) {
        return isValid(data) && CoreUtilities.isFalse(data.object);
    }

    static boolean isTrue(Data<?> data) {
        return isValid(data) && CoreUtilities.isTrue(data.object);
    }

    static boolean isFalse(Data<?> data, int index, int length) {
        return isFalse(data) && BasicPredicateFunctions.isSmallerThan(index, length);
    }

    static boolean isTrue(Data<?> data, int index, int length) {
        return isTrue(data) && BasicPredicateFunctions.isSmallerThan(index, length);
    }

    static boolean isExecuting(Data<?> data, int index, int length) {
        return isValidNonFalse(data) && BasicPredicateFunctions.isSmallerThan(index, length);
    }

    static Object[] unwrapToArray(Data<?> data) {
        return isValidNonFalse(data) ? ArrayUtils.toArray(data.object) : CoreConstants.EMPTY_OBJECT_ARRAY;
    }

    static <T> String getMessageFromData(T object) {
        return (object instanceof Data) ? ((Data<?>) object).message.getMessage() : String.valueOf(object);
    }

    static void throwIfException(Data<?> data) throws Exception {
        if (isNull(data)) {
            return;
        }

        final var exception = data.exception;
        if (isException(exception)) {
            throw exception;
        }
    }
}

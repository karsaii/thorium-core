package com.neathorium.core.extensions.namespaces;

import com.neathorium.core.extensions.namespaces.predicates.BasicPredicates;

import java.util.Objects;

import static com.neathorium.core.extensions.namespaces.NullableFunctions.isNull;

public interface ArrayFunctions {
    static <T> boolean isNullOrEmptyArray(T[] array) {
        if (isNull(array)) {
            return true;
        }

        final var length = array.length;
        final var expectedSize = 1;
        return (
            BasicPredicates.isBiggerThan(expectedSize, length) ||
            (Objects.equals(length, expectedSize) && isNull(array[0]))
        );
    }
}

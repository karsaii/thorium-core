package com.github.karsaii.core.extensions.namespaces;

import java.util.Objects;
import java.util.function.Supplier;

import static com.github.karsaii.core.extensions.namespaces.CoreUtilities.areAll;

public interface SizableFunctions {
    static boolean isSizeEqualTo(int size, int expected) {
        return areAll(BasicPredicateFunctions::isNonNegative, size, expected) && Objects.equals(size, expected);
    }

    static boolean isSizeEqualTo(Supplier<Integer> sizeFunction, int expected) {
        return NullableFunctions.isNotNull(sizeFunction) && isSizeEqualTo(sizeFunction.get(), expected);
    }

    static int size(Supplier<Integer> sizeFunction) {
        return NullableFunctions.isNotNull(sizeFunction) ? sizeFunction.get() : 0;
    }

    static int size(Object[] object) {
        return NullableFunctions.isNotNull(object) ? object.length : 0;
    }
}

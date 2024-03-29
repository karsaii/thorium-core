package com.neathorium.core.extensions.namespaces.predicates;

import com.neathorium.core.extensions.namespaces.NullableFunctions;

import java.util.Objects;
import java.util.function.Supplier;

import static com.neathorium.core.extensions.namespaces.CoreUtilities.areAll;

public interface SizablePredicates {
    static boolean isSizeEqualTo(int size, int expected) {
        return BasicPredicates.isNonNegative(size) && BasicPredicates.isNonNegative(expected) && Objects.equals(size, expected);
    }

    static boolean isSizeEqualTo(Supplier<Integer> sizeFunction, int expected) {
        return NullableFunctions.isNotNull(sizeFunction) && isSizeEqualTo(sizeFunction.get(), expected);
    }
}

package com.github.karsaii.core.extensions.namespaces;

import com.github.karsaii.core.extensions.DecoratedList;

import java.util.Objects;

public interface DecoratedListFunctions {
    static <T, U> boolean isOfTypeNonEmpty(DecoratedList<T> list, Class<U> clazz) {
        return (
            CoreUtilities.areAny(NullableFunctions::isNotNull, list, clazz) &&
            Objects.equals(clazz.getTypeName(), list.getType()) &&
            list.isNotNullAndNonEmpty()
        );
    }
}

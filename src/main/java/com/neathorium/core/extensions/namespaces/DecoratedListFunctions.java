package com.neathorium.core.extensions.namespaces;

import com.neathorium.core.extensions.DecoratedList;

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

package com.github.karsaii.core.extensions.namespaces;

import java.util.Collection;

import static com.github.karsaii.core.extensions.namespaces.EmptiableFunctions.isNotNullAndNonEmpty;
import static com.github.karsaii.core.extensions.namespaces.EmptiableFunctions.isNullOrEmpty;

public interface CollectionPredicateFunctions {
    static <T, U> boolean isEmptyOrNotOfType(Collection<T> collection, Class<U> clazz) {
        return isNullOrEmpty(collection) || !clazz.isInstance(collection.iterator().next());
    }

    static <T, U> boolean isNonEmptyAndOfType(Collection<T> collection, Class<U> clazz) {
        return isNotNullAndNonEmpty(collection) && clazz.isInstance(collection.iterator().next());
    }
}

package com.github.karsaii.core.extensions.namespaces;

import java.util.Collection;
import java.util.Map;

public interface EmptiableFunctions {
    static boolean isEmpty(Collection<?> collection) {
        return NullableFunctions.isNotNull(collection) && collection.isEmpty();
    }

    static boolean isNullOrEmpty(Collection<?> collection) {
        return NullableFunctions.isNull(collection) || collection.isEmpty();
    }

    static boolean isNotNullAndNonEmpty(Collection<?> collection) {
        return !(isNullOrEmpty(collection));
    }

    static boolean isEmpty(Map<?, ?> map) {
        return NullableFunctions.isNotNull(map) && map.isEmpty();
    }

    static boolean isNullOrEmpty(Map<?, ?> map) {
        return NullableFunctions.isNull(map) || map.isEmpty();
    }

    static boolean isNotNullAndNonEmpty(Map<?, ?> map) {
        return !(isNullOrEmpty(map));
    }
}

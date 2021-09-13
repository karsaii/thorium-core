package com.neathorium.core.extensions.namespaces;

import java.util.Objects;

public interface NullableFunctions {
    static boolean isNull(Object object) {
        return Objects.isNull(object);
    }

    static boolean isNotNull(Object object) {
        return !isNull(object);
    }
}

package com.github.karsaii.core.extensions.namespaces;

import java.util.Collection;

public interface EmptiableCollectionFunctions {
    static <T> boolean hasOnlyNonNullValues(Collection<T> collection) {
        return (
            EmptiableFunctions.isNotNullAndNonEmpty(collection) &&
            SizableFunctions.isSizeEqualTo(
                collection::size,
                (int)collection.stream().filter(NullableFunctions::isNotNull).count()
            )
        );
    }
}

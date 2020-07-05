package com.github.karsaii.core.extensions.namespaces.predicates;

import com.github.karsaii.core.extensions.namespaces.CoreUtilities;
import com.github.karsaii.core.namespaces.predicates.DataPredicates;
import com.github.karsaii.core.records.Data;

import static com.github.karsaii.core.extensions.namespaces.NullableFunctions.isNotNull;
import static com.github.karsaii.core.extensions.namespaces.predicates.BasicPredicates.isSmallerThan;

public interface ExecutorPredicates {
    static boolean isFalseStatus(Data<?> data, int index, int length) {
        return isNotNull(data) && CoreUtilities.isFalse(data.status) && isSmallerThan(index, length);
    }

    static boolean isExecuting(Data<?> data, int index, int length) {
        return DataPredicates.isValidNonFalse(data) && isSmallerThan(index, length);
    }
}

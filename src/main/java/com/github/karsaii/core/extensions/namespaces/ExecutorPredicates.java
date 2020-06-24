package com.github.karsaii.core.extensions.namespaces;

import com.github.karsaii.core.extensions.namespaces.predicates.BasicPredicateFunctions;
import com.github.karsaii.core.records.Data;

import static com.github.karsaii.core.extensions.namespaces.NullableFunctions.isNotNull;
import static com.github.karsaii.core.namespaces.validators.DataValidators.isValidNonFalse;

public interface ExecutorPredicates {
    static boolean isFalse(Data<?> data, int index, int length) {
        return isNotNull(data) && CoreUtilities.isFalse(data.status) && BasicPredicateFunctions.isSmallerThan(index, length);
    }

    static boolean isExecuting(Data<?> data, int index, int length) {
        return isValidNonFalse(data) && BasicPredicateFunctions.isSmallerThan(index, length);
    }
}

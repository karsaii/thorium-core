package com.github.karsaii.core.namespaces.predicates;

import com.github.karsaii.core.records.MethodMessageData;

import static com.github.karsaii.core.extensions.namespaces.NullableFunctions.isNotNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public interface MethodMessageDataPredicates {
    static boolean isValid(MethodMessageData data) {
        return isNotNull(data) && isNotBlank(data.message) && isNotNull(data.nameof);
    }
}

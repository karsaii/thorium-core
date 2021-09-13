package com.neathorium.core.namespaces.predicates;

import com.neathorium.core.records.MethodMessageData;
import com.neathorium.core.extensions.namespaces.NullableFunctions;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public interface MethodMessageDataPredicates {
    static boolean isValid(MethodMessageData data) {
        return NullableFunctions.isNotNull(data) && isNotBlank(data.message) && NullableFunctions.isNotNull(data.nameof);
    }
}

package com.neathorium.core.namespaces.executor;

import com.neathorium.core.records.Data;
import com.neathorium.core.records.executor.ExecutionResultData;
import com.neathorium.core.records.executor.ExecutionStateData;
import com.neathorium.core.extensions.namespaces.NullableFunctions;

import java.util.List;
import java.util.Map;

public interface ExecutionResultDataFactory {
    static <T> ExecutionResultData<T> getWith(ExecutionStateData data, T object) {
        final var lData = NullableFunctions.isNotNull(data) ? data : ExecutionStateDataFactory.getWithDefaults();
        return new ExecutionResultData<>(lData, object);
    }

    static <T> ExecutionResultData<T> getWith(Map<String, Data<?>> map, List<Integer> indices, T object) {
        return getWith(ExecutionStateDataFactory.getWith(map, indices), object);
    }

    static <T> ExecutionResultData<T> getWithDefaultState(T object) {
        return getWith(ExecutionStateDataFactory.getWithDefaults(), object);
    }
}

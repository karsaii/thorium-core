package com.github.karsaii.core.namespaces.executor;

import com.github.karsaii.core.records.Data;
import com.github.karsaii.core.records.executor.ExecutionResultData;
import com.github.karsaii.core.records.executor.ExecutionStateData;

import java.util.List;
import java.util.Map;

import static com.github.karsaii.core.extensions.namespaces.NullableFunctions.isNotNull;

public interface ExecutionResultDataFactory {
    static <T> ExecutionResultData<T> getWith(ExecutionStateData data, T object) {
        final var lData = isNotNull(data) ? data : ExecutionStateDataFactory.getWithDefaults();
        return new ExecutionResultData<>(lData, object);
    }

    static <T> ExecutionResultData<T> getWith(Map<String, Data<?>> map, List<Integer> indices, T object) {
        return getWith(ExecutionStateDataFactory.getWith(map, indices), object);
    }

    static <T> ExecutionResultData<T> getWithDefaultState(T object) {
        return getWith(ExecutionStateDataFactory.getWithDefaults(), object);
    }
}

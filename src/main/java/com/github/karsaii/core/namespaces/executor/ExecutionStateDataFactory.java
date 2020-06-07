package com.github.karsaii.core.namespaces.executor;

import com.github.karsaii.core.extensions.namespaces.BasicPredicateFunctions;
import com.github.karsaii.core.records.Data;
import com.github.karsaii.core.records.executor.ExecutionStateData;
import org.eclipse.collections.impl.list.Interval;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.github.karsaii.core.extensions.namespaces.NullableFunctions.isNotNull;

public interface ExecutionStateDataFactory {
    private static Map<String, Data<?>> getDefaultMap() {
        return new LinkedHashMap<>();
    }

    private static List<Integer> getDefaultList() {
        return new ArrayList<>();
    }

    private static List<Integer> getListWithIndices(int length) {
        final var localLength = BasicPredicateFunctions.isPositiveNonZero(length) ? length : 0;
        return Interval.zeroTo(localLength > 0 ? localLength - 1 : localLength).toList();
    }

    static ExecutionStateData getWith(Map<String, Data<?>> map, List<Integer> indices) {
        return new ExecutionStateData(
            isNotNull(map) ? map : getDefaultMap(),
            isNotNull(indices) ? indices : getDefaultList()
        );
    }

    static ExecutionStateData getWithDefaults() {
        return getWith(getDefaultMap(), getDefaultList());
    }

    static ExecutionStateData getWithDefaultIndices(Map<String, Data<?>> map) {
        return getWith(map, getDefaultList());
    }

    static ExecutionStateData getWithDefaultMap(List<Integer> indices) {
        return getWith(getDefaultMap(), indices);
    }

    static ExecutionStateData getWithDefaultMapAndSpecificLength(int length) {
        final var localLength = BasicPredicateFunctions.isPositiveNonZero(length) ? length : 0;
        return getWithDefaultMap(getListWithIndices(localLength));
    }

    static ExecutionStateData getWith(Map<String, Data<?>> map, int length) {
        return getWith(map, getListWithIndices(length));
    }
}

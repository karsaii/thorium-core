package com.github.karsaii.core.namespaces.factories.wait.tasks.common;

import com.github.karsaii.core.records.Data;
import com.github.karsaii.core.records.wait.tasks.common.WaitTaskStateData;

import java.util.concurrent.atomic.AtomicInteger;

public interface WaitTaskStateDataFactory {
    static <T, V> WaitTaskStateData<T, V> getWith(Data<V> data, T dependency, AtomicInteger counter, int limit) {
        return new WaitTaskStateData<>(data, dependency, counter, limit);
    }

    static <T, V> WaitTaskStateData<T, V> getWithDefaultLimit(Data<V> data, T dependency, AtomicInteger counter) {
        return getWith(data, dependency, counter, 1);
    }

    static <T, V> WaitTaskStateData<T, V> getWithDefaultCounter(Data<V> data, T dependency, int limit) {
        return getWith(data, dependency, new AtomicInteger(), limit);
    }

    static <T, V> WaitTaskStateData<T, V> getWithDefaultCounterAndLimit(Data<V> data, T dependency) {
        return getWithDefaultCounter(data, dependency, 1);
    }
}

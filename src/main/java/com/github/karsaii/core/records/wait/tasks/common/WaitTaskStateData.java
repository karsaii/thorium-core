package com.github.karsaii.core.records.wait.tasks.common;

import com.github.karsaii.core.extensions.namespaces.CoreUtilities;
import com.github.karsaii.core.extensions.namespaces.NullableFunctions;

import java.util.Objects;

public class WaitTaskStateData<T, V> {
    public WaitTaskReturnData<V> data;
    public T dependency;

    public WaitTaskStateData(WaitTaskReturnData<V> data, T dependency) {
        this.data = data;
        this.dependency = dependency;
    }

    @Override
    public boolean equals(Object o) {
        if (CoreUtilities.isEqual(this, o)) {
            return true;
        }

        if (NullableFunctions.isNull(o) || CoreUtilities.isNotEqual(getClass(), o.getClass())) {
            return false;
        }

        final var that = (WaitTaskStateData<?, ?>) o;
        return CoreUtilities.isEqual(data, that.data) && CoreUtilities.isEqual(dependency, that.dependency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, dependency);
    }
}

package com.neathorium.core.extensions.records;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class ExtensionListData<T> {
    public final Function<List<T>, T> getter;
    public final Function<List<T>, Integer> endIndexFunction;
    public final int startIndex;

    public ExtensionListData(Function<List<T>, T> getter, Function<List<T>, Integer> endIndexFunction, int startIndex) {
        this.getter = getter;
        this.endIndexFunction = endIndexFunction;
        this.startIndex = startIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final var that = (ExtensionListData<?>) o;
        return startIndex == that.startIndex && Objects.equals(getter, that.getter) && Objects.equals(endIndexFunction, that.endIndexFunction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getter, endIndexFunction, startIndex);
    }
}

package com.github.karsaii.core.records.executor;

import com.github.karsaii.core.extensions.interfaces.functional.boilers.DataSupplier;
import com.github.karsaii.core.records.Data;

import java.util.Objects;
import java.util.function.Function;

public class ExecutionStepData<T, U> implements DataSupplier<U> {
    public final Function<T, Data<U>> step;
    public final T dependency;

    public ExecutionStepData(Function<T, Data<U>> step, T dependency) {
        this.step = step;
        this.dependency = dependency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final var that = (ExecutionStepData<?, ?>) o;
        return Objects.equals(step, that.step) && Objects.equals(dependency, that.dependency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(step, dependency);
    }

    @Override
    public Data<U> apply() {
        return step.apply(dependency);
    }

    @Override
    public Data<U> apply(Void aVoid) {
        return apply();
    }
}

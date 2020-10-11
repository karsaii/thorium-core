package com.github.karsaii.core.records.executor;

import com.github.karsaii.core.extensions.interfaces.functional.boilers.DataSupplier;
import com.github.karsaii.core.extensions.namespaces.CoreUtilities;
import com.github.karsaii.core.extensions.namespaces.NullableFunctions;
import com.github.karsaii.core.records.Data;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class ExecutionStepData<T, U> implements DataSupplier<U> {
    public final Function<T, Data<U>> step;
    public final Supplier<T> dependency;

    public ExecutionStepData(Function<T, Data<U>> step, Supplier<T> dependency) {
        this.step = step;
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

        final var that = (ExecutionStepData<?, ?>) o;
        return CoreUtilities.isEqual(step, that.step) && CoreUtilities.isEqual(dependency, that.dependency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(step, dependency);
    }

    @Override
    public Data<U> apply() {
        return step.apply(dependency.get());
    }

    @Override
    public Data<U> apply(Void o) {
        return apply();
    }
}

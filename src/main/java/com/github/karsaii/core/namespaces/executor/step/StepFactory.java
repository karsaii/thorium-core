package com.github.karsaii.core.namespaces.executor.step;

import com.github.karsaii.core.extensions.interfaces.functional.boilers.DataSupplier;
import com.github.karsaii.core.records.Data;
import com.github.karsaii.core.records.executor.ExecutionKeyStepData;
import com.github.karsaii.core.records.executor.ExecutionStepData;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public interface StepFactory {
    static <DependencyType, ReturnType> DataSupplier<ReturnType> step(Function<DependencyType, Data<ReturnType>> function, DependencyType dependency) {
        return new ExecutionStepData<>(function, () -> dependency);
    }

    static <DependencyType, ReturnType> DataSupplier<ReturnType> voidStep(Function<DependencyType, Data<ReturnType>> function) {
        return new ExecutionStepData<>(function, () -> null);
    }

    static <ReturnType> DataSupplier<ReturnType> voidStep(Supplier<Data<ReturnType>> supplier) {
        return new ExecutionStepData<Void, ReturnType>((v) -> supplier.get(), () -> null);
    }

    static <DependencyType, KeyType, ReturnType> DataSupplier<ReturnType> step(Function<DependencyType, Data<ReturnType>> function, Function<KeyType, DependencyType> dependencyGetter, KeyType key) {
        return new ExecutionKeyStepData<>(function, dependencyGetter, key);
    }

    static <DependencyType, KeyType, ReturnType> BiFunction<Function<DependencyType, Data<ReturnType>>, KeyType, DataSupplier<ReturnType>> step(Function<KeyType, DependencyType> dependencyGetter) {
        return (function, dependencyKey) -> new ExecutionKeyStepData<>(function, dependencyGetter, dependencyKey);
    }
}

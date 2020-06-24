package com.github.karsaii.core.namespaces.executor.step;

import com.github.karsaii.core.extensions.interfaces.functional.boilers.DataSupplier;
import com.github.karsaii.core.records.Data;
import com.github.karsaii.core.records.executor.ExecutionStepData;

import java.util.function.Function;

public interface StepFactory {
    static <DependencyType, ReturnType> DataSupplier<ReturnType> step(Function<DependencyType, Data<ReturnType>> function, DependencyType dependency) {
        return new ExecutionStepData<>(function, dependency);
    }

    static <DependencyType, ReturnType> DataSupplier<ReturnType> step(Function<DependencyType, Data<ReturnType>> function) {
        return new ExecutionStepData<>(function, null);
    }
}

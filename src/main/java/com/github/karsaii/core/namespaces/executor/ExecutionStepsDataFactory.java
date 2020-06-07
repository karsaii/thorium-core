package com.github.karsaii.core.namespaces.executor;

import com.github.karsaii.core.extensions.namespaces.BasicPredicateFunctions;
import com.github.karsaii.core.extensions.namespaces.NullableFunctions;
import com.github.karsaii.core.records.Data;
import com.github.karsaii.core.records.executor.ExecutionStepsData;

import java.util.function.Function;

public interface ExecutionStepsDataFactory {
    static <DependencyType> ExecutionStepsData<DependencyType> getWith(Function<DependencyType, Data<?>>[] steps, DependencyType dependency, int length) {
        final var localLength = BasicPredicateFunctions.isNonNegative(length) ? length : 0;
        return new ExecutionStepsData<>(steps, dependency, localLength);
    }

    static <DependencyType> ExecutionStepsData<DependencyType> getWithStepsAndDependency(Function<DependencyType, Data<?>>[] steps, DependencyType dependency) {
        if (NullableFunctions.isNull(steps)) {
            return getWith(steps, dependency, 0);
        }

        final var stepLength = steps.length;
        final var length = BasicPredicateFunctions.isNonNegative(stepLength) ? stepLength : 0;
        return getWith(steps, dependency, length);
    }
}

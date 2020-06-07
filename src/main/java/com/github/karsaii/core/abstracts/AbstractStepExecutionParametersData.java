package com.github.karsaii.core.abstracts;

import com.github.karsaii.core.extensions.interfaces.functional.TriFunction;
import com.github.karsaii.core.extensions.interfaces.functional.boilers.DataSupplier;
import com.github.karsaii.core.records.Data;
import com.github.karsaii.core.records.command.CommandRangeData;
import com.github.karsaii.core.records.executor.ExecutionResultData;
import com.github.karsaii.core.records.executor.ExecutionStateData;
import com.github.karsaii.core.records.executor.ExecutorFunctionData;

import java.util.Objects;
import java.util.function.Function;

public abstract class AbstractStepExecutionParametersData<ReturnType> {
    public final ExecutorFunctionData data;
    public final TriFunction<ExecutorFunctionData, ExecutionStateData, Function<Void, Data<?>>[], DataSupplier<ExecutionResultData<ReturnType>>> executor;
    public final CommandRangeData range;

    public AbstractStepExecutionParametersData(ExecutorFunctionData data, TriFunction<ExecutorFunctionData, ExecutionStateData, Function<Void, Data<?>>[], DataSupplier<ExecutionResultData<ReturnType>>> executor, CommandRangeData range) {
        this.data = data;
        this.executor = executor;
        this.range = range;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final var that = (AbstractStepExecutionParametersData<?>) o;
        return Objects.equals(data, that.data) && Objects.equals(executor, that.executor) && Objects.equals(range, that.range);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, executor, range);
    }
}

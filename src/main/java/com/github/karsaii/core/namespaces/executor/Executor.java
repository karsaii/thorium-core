package com.github.karsaii.core.namespaces.executor;

import com.github.karsaii.core.constants.CoreDataConstants;
import com.github.karsaii.core.constants.CoreConstants;
import com.github.karsaii.core.constants.wait.WaitConstants;
import com.github.karsaii.core.exceptions.ArgumentNullException;
import com.github.karsaii.core.extensions.interfaces.functional.boilers.DataSupplier;
import com.github.karsaii.core.extensions.namespaces.NullableFunctions;
import com.github.karsaii.core.namespaces.DataExecutionFunctions;
import com.github.karsaii.core.namespaces.DataFactoryFunctions;
import com.github.karsaii.core.namespaces.ExceptionHandlers;
import com.github.karsaii.core.namespaces.predicates.DataPredicates;
import com.github.karsaii.core.records.Data;
import com.github.karsaii.core.records.executor.ExecutionResultData;
import com.github.karsaii.core.records.executor.ExecutionStateData;
import com.github.karsaii.core.records.executor.ExecutionStepsData;
import com.github.karsaii.core.records.executor.ExecutorFunctionData;
import com.github.karsaii.core.records.executor.ExecutionParametersData;
import com.github.karsaii.core.constants.validators.CoreFormatterConstants;
import com.github.karsaii.core.namespaces.validators.CoreFormatter;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static com.github.karsaii.core.namespaces.predicates.DataPredicates.isInvalidOrFalse;

public interface Executor {
    private static <DependencyType, ReturnType> Data<ExecutionResultData<ReturnType>> executeCore(
        ExecutionStepsData<DependencyType> stepsData,
        ExecutorFunctionData functionData,
        ExecutionStateData stateData
    ) {
        Data<?> data = CoreDataConstants.NO_STEPS;
        final var exitCondition = functionData.breakCondition;
        final var filter = functionData.filterCondition;
        final var indices = stateData.indices;
        final var map = stateData.executionMap;
        final var length = indices.size();
        final var steps = stepsData.steps;
        final var dependency = stepsData.dependency;
        var stepIndex = 0;
        var index = 0;
        var key = "";
        while (exitCondition.test(data, index, indices.size())) {
            stepIndex = indices.get(index);
            data = steps[stepIndex].apply(dependency);
            key = CoreFormatter.getExecutionResultKey(data.message.nameof, stepIndex);
            if (!map.containsKey(key) || isInvalidOrFalse(map.get(key))) {
                map.put(key, data);
            }

            if (filter.test(data)) {
                indices.remove(index);
            } else {
                ++index;
            }
        }

        final var executionStatus = ExecutionStateDataFactory.getWith(map, indices);
        final var status = functionData.endCondition.test(executionStatus, steps.length, index, indices.size());
        final var message = functionData.messageData.get().apply(status) + CoreFormatterConstants.COLON_NEWLINE + functionData.endMessageHandler.apply(executionStatus, key, index, length);
        @SuppressWarnings("unchecked")
        final var returnObject = (ReturnType)data.object;
        return DataFactoryFunctions.getWithNameAndMessage(ExecutionResultDataFactory.getWith(executionStatus, returnObject), status, "executeCore", message);
    }

    @SafeVarargs
    private static <DependencyType, ArrayType, ReturnType, ParameterReturnType> Function<DependencyType, Data<ReturnType>> executeGuardCore(
        ExecutionParametersData<ArrayType, Function<DependencyType, Data<ParameterReturnType>>> execution,
        Function<DependencyType, Data<ReturnType>> executionChain,
        Data<ReturnType> negative,
        Function<DependencyType, Data<?>>... steps
    ) {
        return DataExecutionFunctions.ifDependency("executeGuardCore", CoreFormatter.getValidCommandMessage(steps, execution.range), executionChain, negative);
    }

    @SafeVarargs
    static <DependencyType, ReturnType> Function<DependencyType, Data<ExecutionResultData<ReturnType>>> execute(
        ExecutionParametersData<Function<DependencyType, Data<?>>, Function<DependencyType, Data<ExecutionResultData<ReturnType>>>> execution,
        ExecutionStateData stateData,
        Function<DependencyType, Data<?>>... steps
    ) {
        @SuppressWarnings("unchecked")
        final var negativeReturnObject = (ReturnType) CoreConstants.STOCK_OBJECT;
        final var negative = DataFactoryFunctions.getInvalidWithNameAndMessage(ExecutionResultDataFactory.getWithDefaultState(negativeReturnObject), "execute", CoreFormatterConstants.EMPTY);
        return executeGuardCore(execution, execution.executor.apply(execution.functionData, stateData, steps), negative, steps);
    }

    private static <DependencyType, ReturnType> Data<ReturnType> executeData(
        ExecutionStepsData<DependencyType> stepsData,
        ExecutionParametersData<Function<DependencyType, Data<?>>, Function<DependencyType, Data<ExecutionResultData<ReturnType>>>> execution
    ) {
        final var result = execute(execution, ExecutionStateDataFactory.getWithDefaultMapAndSpecificLength(stepsData.length), stepsData.steps).apply(stepsData.dependency);
        return DataFactoryFunctions.replaceObject(result, result.object.result);
    }

    @SafeVarargs
    private static <DependencyType, ReturnType> Function<DependencyType, Data<ReturnType>> executeData(
        ExecutionParametersData<Function<DependencyType, Data<?>>, Function<DependencyType, Data<ExecutionResultData<ReturnType>>>> execution,
        Function<DependencyType, Data<?>>... steps
    ) {
        return dependency -> executeData(ExecutionStepsDataFactory.getWithStepsAndDependency(steps, dependency), execution);
    }

    @SafeVarargs
    static <DependencyType, ReturnType> Function<DependencyType, Data<ExecutionResultData<ReturnType>>> execute(ExecutorFunctionData functionData, ExecutionStateData stateData, Function<DependencyType, Data<?>>... steps) {
        return dependency -> executeCore(ExecutionStepsDataFactory.getWithStepsAndDependency(steps, dependency), functionData, stateData);
    }

    @SafeVarargs
    static <DependencyType, ReturnType> Function<DependencyType, Data<ExecutionResultData<ReturnType>>> execute(ExecutorFunctionData functionData, Function<DependencyType, Data<?>>... steps) {
        return dependency -> executeCore(ExecutionStepsDataFactory.getWithStepsAndDependency(steps, dependency), functionData, ExecutionStateDataFactory.getWithDefaultMapAndSpecificLength(steps.length));
    }

    @SafeVarargs
    static <DependencyType, ReturnType> Function<DependencyType, Data<ReturnType>> execute(ExecutionParametersData<Function<DependencyType, Data<?>>, Function<DependencyType, Data<ExecutionResultData<ReturnType>>>> execution, Function<DependencyType, Data<?>>... steps) {
        @SuppressWarnings("unchecked")
        final var negativeReturnObject = (ReturnType) CoreConstants.STOCK_OBJECT;
        final var negative = DataFactoryFunctions.getWithMessage(negativeReturnObject, false, CoreFormatterConstants.EMPTY);
        return executeGuardCore(execution, executeData(execution, steps), negative, steps);
    }
}

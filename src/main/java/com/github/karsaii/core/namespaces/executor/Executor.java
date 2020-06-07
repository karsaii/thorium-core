package com.github.karsaii.core.namespaces.executor;

import com.github.karsaii.core.constants.CoreDataConstants;
import com.github.karsaii.core.constants.CoreConstants;
import com.github.karsaii.core.extensions.namespaces.BasicPredicateFunctions;
import com.github.karsaii.core.extensions.namespaces.CoreUtilities;
import com.github.karsaii.core.namespaces.DataFactoryFunctions;
import com.github.karsaii.core.records.Data;
import com.github.karsaii.core.records.executor.ExecutionResultData;
import com.github.karsaii.core.records.executor.ExecutionStateData;
import com.github.karsaii.core.records.executor.ExecutionStepsData;
import com.github.karsaii.core.records.executor.ExecutorFunctionData;
import com.github.karsaii.core.records.executor.ExecutionParametersData;
import com.github.karsaii.core.constants.validators.CoreFormatterConstants;
import com.github.karsaii.core.namespaces.validators.CoreFormatter;

import java.util.function.Function;

import static com.github.karsaii.core.extensions.namespaces.NullableFunctions.isNotNull;
import static com.github.karsaii.core.namespaces.validators.DataValidators.isInvalidOrFalse;
import static com.github.karsaii.core.namespaces.validators.DataValidators.isValidNonFalse;
import static com.github.karsaii.core.namespaces.DependencyExecutionFunctions.ifDependency;

public interface Executor {
    static boolean isFalse(Data<?> data, int index, int length) {
        return (
            isNotNull(data) &&
            CoreUtilities.isFalse(data.status) &&
            BasicPredicateFunctions.isSmallerThan(index, length)
        );
    }

    static boolean isExecuting(Data<?> data, int index, int length) {
        return isValidNonFalse(data) && BasicPredicateFunctions.isSmallerThan(index, length);
    }

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
        for (; exitCondition.test(data, index, indices.size());) {
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
        return DataFactoryFunctions.getWithNameAndMessage(ExecutionResultDataFactory.getWith(executionStatus, (ReturnType)data.object), status, "executeCore", message);
    }

    private static <DependencyType, ArrayType, ReturnType, ParameterReturnType> Function<DependencyType, Data<ReturnType>> executeGuardCore(
        ExecutionParametersData<ArrayType, Function<DependencyType, Data<ParameterReturnType>>> execution,
        Function<DependencyType, Data<ReturnType>> executionChain,
        Data<ReturnType> negative,
        int stepLength
    ) {
        return ifDependency("executeGuardCore", CoreFormatter.getCommandAmountRangeErrorMessage(stepLength, execution.range), executionChain, negative);
    }

    @SafeVarargs
    static <DependencyType, ReturnType> Function<DependencyType, Data<ExecutionResultData<ReturnType>>> execute(
        ExecutionParametersData<Function<DependencyType, Data<?>>, Function<DependencyType, Data<ExecutionResultData<ReturnType>>>> execution,
        ExecutionStateData stateData,
        Function<DependencyType, Data<?>>... steps
    ) {
        final var negative = DataFactoryFunctions.getWithMessage(ExecutionResultDataFactory.getWithDefaultState((ReturnType) CoreConstants.STOCK_OBJECT), false, CoreFormatterConstants.EMPTY);
        return executeGuardCore(execution, execution.executor.apply(execution.functionData, stateData, steps), negative, steps.length);
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
        final var negative = DataFactoryFunctions.getWithMessage((ReturnType) CoreConstants.STOCK_OBJECT, false, CoreFormatterConstants.EMPTY);
        return executeGuardCore(execution, executeData(execution, steps), negative, steps.length);
    }
}

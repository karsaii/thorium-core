package com.neathorium.core.namespaces.executor.step;

import com.neathorium.core.constants.CoreConstants;
import com.neathorium.core.constants.ExecutorConstants;
import com.neathorium.core.constants.wait.WaitConstants;
import com.neathorium.core.exceptions.ArgumentNullException;
import com.neathorium.core.extensions.interfaces.functional.QuadFunction;
import com.neathorium.core.extensions.interfaces.functional.TriPredicate;
import com.neathorium.core.extensions.interfaces.functional.boilers.DataSupplier;
import com.neathorium.core.extensions.interfaces.functional.boilers.IGetMessage;
import com.neathorium.core.extensions.namespaces.CoreUtilities;
import com.neathorium.core.extensions.namespaces.NullableFunctions;
import com.neathorium.core.extensions.namespaces.concurrent.CompletableFutureExtensions;
import com.neathorium.core.namespaces.DataFactoryFunctions;
import com.neathorium.core.namespaces.DataSupplierExecutionFunctions;
import com.neathorium.core.namespaces.exception.TaskExceptionHandlers;
import com.neathorium.core.namespaces.factories.DataSupplierFactory;
import com.neathorium.core.namespaces.executor.ExecutorFunctionDataFactory;
import com.neathorium.core.namespaces.executor.ExecutionParametersDataFactory;
import com.neathorium.core.namespaces.executor.ExecutionResultDataFactory;
import com.neathorium.core.namespaces.executor.ExecutionStateDataFactory;
import com.neathorium.core.namespaces.executor.ExecutionStepsDataFactory;
import com.neathorium.core.namespaces.executor.Executor;
import com.neathorium.core.namespaces.formatter.executor.StepExecutorFormatters;
import com.neathorium.core.namespaces.task.TaskUtilities;
import com.neathorium.core.records.Data;
import com.neathorium.core.records.SimpleMessageData;
import com.neathorium.core.records.executor.ExecutionParametersData;
import com.neathorium.core.records.executor.ExecutionResultData;
import com.neathorium.core.records.executor.ExecutionStateData;
import com.neathorium.core.records.executor.ExecutionStepsData;
import com.neathorium.core.constants.validators.CoreFormatterConstants;
import com.neathorium.core.namespaces.validators.CoreFormatter;

import java.util.Arrays;
import java.util.function.Function;

public interface StepExecutor {
    private static <ReturnType, ParameterReturnType> DataSupplier<ReturnType> executeGuardCore(
        ExecutionParametersData<Function<Void, Data<?>>, DataSupplier<ParameterReturnType>> execution,
        DataSupplier<ReturnType> executionChain,
        Data<ReturnType> negative,
        int stepLength
    ) {
        return DataSupplierExecutionFunctions.ifDependency("executeGuardCore", CoreFormatter.getCommandAmountRangeErrorMessage(stepLength, execution.range), executionChain, negative);
    }

    @SafeVarargs
    static <ReturnType> DataSupplier<ExecutionResultData<ReturnType>> execute(
        ExecutionParametersData<Function<Void, Data<?>>, DataSupplier<ExecutionResultData<ReturnType>>> execution,
        ExecutionStateData stateData,
        Function<Void, Data<?>>... steps
    ) {
        @SuppressWarnings("unchecked")
        final var negativeReturnObject = (ReturnType) CoreConstants.STOCK_OBJECT;
        final var negative = DataFactoryFunctions.getInvalidWith(ExecutionResultDataFactory.getWithDefaultState(negativeReturnObject), "execute", CoreFormatterConstants.EMPTY);
        return executeGuardCore(execution, DataSupplierFactory.get(execution.executor.apply(execution.functionData, stateData, steps)), negative, steps.length);
    }

    private static <ReturnType> Data<ReturnType> executeData(
        ExecutionStepsData<Void> stepsData,
        ExecutionParametersData<Function<Void, Data<?>>, DataSupplier<ExecutionResultData<ReturnType>>> execution
    ) {
        final var result = execute(execution, ExecutionStateDataFactory.getWithDefaults(), stepsData.steps).apply(stepsData.dependency);
        return DataFactoryFunctions.replaceObject(result, result.object.result);
    }

    private static <ReturnType> DataSupplier<ReturnType> executeData(
        ExecutionParametersData<Function<Void, Data<?>>, DataSupplier<ExecutionResultData<ReturnType>>> execution,
        DataSupplier<?>... steps
    ) {
        return DataSupplierFactory.get(dependency -> executeData(ExecutionStepsDataFactory.getWithStepsAndDependency(steps, dependency), execution));
    }

    static <ReturnType> DataSupplier<ReturnType> execute(ExecutionParametersData<Function<Void, Data<?>>, DataSupplier<ExecutionResultData<ReturnType>>> execution, DataSupplier<?>... steps) {
        @SuppressWarnings("unchecked")
        final var negativeReturnObject = (ReturnType) CoreConstants.STOCK_OBJECT;
        final var negative = DataFactoryFunctions.getInvalidWith(negativeReturnObject, "execute", CoreFormatterConstants.EMPTY);
        return executeGuardCore(execution, executeData(execution, steps), negative, steps.length);
    }

    static <ReturnType> DataSupplier<ReturnType> execute(IGetMessage stepMessage, DataSupplier<?>... steps) {
        return DataSupplierFactory.get(Executor.execute(
            ExecutionParametersDataFactory.getWithDefaultRange(
                ExecutorFunctionDataFactory.getWithExecuteParametersDataAndDefaultExitCondition(stepMessage, ExecutorConstants.DEFAULT_EXECUTION_DATA),
                Executor::execute
            ),
            steps
        ));
    }

    static <ReturnType> DataSupplier<ReturnType> execute(String message, DataSupplier<?>... steps) {
        return DataSupplierFactory.get(Executor.execute(
            ExecutionParametersDataFactory.getWithDefaultRange(
                ExecutorFunctionDataFactory.getWithSpecificMessage(message),
                Executor::execute
            ),
            steps
        ));
    }

    static <ReturnType> DataSupplier<ReturnType> execute(QuadFunction<ExecutionStateData, String, Integer, Integer, String> messageHandler, DataSupplier<?>... steps) {
        return DataSupplierFactory.get(Executor.execute(
            ExecutionParametersDataFactory.getWithTwoCommandsRange(
                ExecutorFunctionDataFactory.getWithDefaultExitConditionAndMessageData(messageHandler),
                Executor::execute
            ),
            steps
        ));
    }

    static <ReturnType> DataSupplier<ReturnType> execute(DataSupplier<?>... steps) {
        return DataSupplierFactory.get(Executor.execute(ExecutionParametersDataFactory.getWithDefaultFunctionDataAndDefaultRange(Executor::execute), steps));
    }

    static <ReturnType> DataSupplier<ReturnType> conditionalSequence(TriPredicate<Data<?>, Integer, Integer> guard, DataSupplier<?> before, DataSupplier<?> after) {
        final DataSupplier<?>[] steps = Arrays.asList(before, after).toArray(new DataSupplier<?>[0]);
        return DataSupplierFactory.get(Executor.execute(
            ExecutionParametersDataFactory.getWithTwoCommandsRange(
                ExecutorFunctionDataFactory.getWithSpecificMessageDataAndBreakCondition(new SimpleMessageData(CoreFormatterConstants.EXECUTION_ENDED), guard),
                Executor::execute
            ),
            steps
        ));
    }

    static <T, U, ReturnType> DataSupplier<ExecutionResultData<ReturnType>> conditionalSequence(DataSupplier<T> before, DataSupplier<U> after, Class<ReturnType> clazz) {
        final DataSupplier<?>[] steps = Arrays.asList(before, after).toArray(new DataSupplier<?>[0]);
        return DataSupplierFactory.get(Executor.execute(
            ExecutionParametersDataFactory.getWithDefaultFunctionDataAndTwoCommandRange(Executor::execute),
            steps
        ));
    }

    static <ReturnType> DataSupplier<ExecutionResultData<ReturnType>> execute(IGetMessage stepMessage, ExecutionStateData stateData, DataSupplier<?>... steps) {
        final var localStateData = (NullableFunctions.isNotNull(stateData.indices) && CoreUtilities.isFalse(stateData.indices.isEmpty())) ? stateData : ExecutionStateDataFactory.getWith(stateData.executionMap, steps.length);
        return DataSupplierFactory.get(Executor.execute(
            ExecutionParametersDataFactory.getWithDefaultRange(
                ExecutorFunctionDataFactory.getWithExecuteParametersDataAndDefaultExitCondition(stepMessage, ExecutorConstants.DEFAULT_EXECUTION_DATA),
                Executor::execute
            ),
            localStateData,
            steps
        ));
    }

    static <ReturnType> DataSupplier<ExecutionResultData<ReturnType>> execute(String message, ExecutionStateData stateData, DataSupplier<?>... steps) {
        final var localStateData = (NullableFunctions.isNotNull(stateData) && NullableFunctions.isNotNull(stateData.indices) && CoreUtilities.isFalse(stateData.indices.isEmpty())) ? stateData : ExecutionStateDataFactory.getWith(stateData.executionMap, steps.length);
        return DataSupplierFactory.get(Executor.execute(
            ExecutionParametersDataFactory.getWithDefaultRange(
                ExecutorFunctionDataFactory.getWithSpecificMessage(message),
                Executor::execute
            ),
            localStateData,
            steps
        ));
    }

    static <ReturnType> DataSupplier<ExecutionResultData<ReturnType>> execute(QuadFunction<ExecutionStateData, String, Integer, Integer, String> messageHandler, ExecutionStateData stateData, DataSupplier<?>... steps) {
        final var localStateData = (NullableFunctions.isNotNull(stateData.indices) && CoreUtilities.isFalse(stateData.indices.isEmpty())) ? stateData : ExecutionStateDataFactory.getWith(stateData.executionMap, steps.length);
        return DataSupplierFactory.get(Executor.execute(
            ExecutionParametersDataFactory.getWithTwoCommandsRange(
                ExecutorFunctionDataFactory.getWithDefaultExitConditionAndMessageData(messageHandler),
                Executor::execute
            ),
            localStateData,
            steps
        ));
    }

    static <ReturnType> DataSupplier<ExecutionResultData<ReturnType>> execute(ExecutionStateData stateData, DataSupplier<?>... steps) {
        final var localStateData = (NullableFunctions.isNotNull(stateData.indices) && CoreUtilities.isFalse(stateData.indices.isEmpty())) ? stateData : ExecutionStateDataFactory.getWith(stateData.executionMap, steps.length);
        return DataSupplierFactory.get(Executor.execute(ExecutionParametersDataFactory.getWithDefaultFunctionDataAndDefaultRange(Executor::execute), localStateData, steps));
    }


    static <ReturnType> DataSupplier<ExecutionResultData<ReturnType>> conditionalSequence(TriPredicate<Data<?>, Integer, Integer> guard, ExecutionStateData stateData, DataSupplier<?> before, DataSupplier<?> after) {
        final DataSupplier<?>[] steps = Arrays.asList(before, after).toArray(new DataSupplier<?>[0]);
        final var localStateData = (NullableFunctions.isNotNull(stateData.indices) && CoreUtilities.isFalse(stateData.indices.isEmpty())) ? stateData : ExecutionStateDataFactory.getWith(stateData.executionMap, steps.length);
        return DataSupplierFactory.get(Executor.execute(
            ExecutionParametersDataFactory.getWithTwoCommandsRange(
                ExecutorFunctionDataFactory.getWithSpecificMessageDataAndBreakCondition(new SimpleMessageData(CoreFormatterConstants.EXECUTION_ENDED), guard),
                Executor::execute
            ),
            localStateData,
            steps
        ));
    }

    static <T, U, ReturnType> DataSupplier<ExecutionResultData<ReturnType>> conditionalSequence(ExecutionStateData stateData, DataSupplier<T> before, DataSupplier<U> after, Class<ReturnType> clazz) {
        final DataSupplier<?>[] steps = Arrays.asList(before, after).toArray(new DataSupplier<?>[0]);
        final var localStateData = (NullableFunctions.isNotNull(stateData.indices) && CoreUtilities.isFalse(stateData.indices.isEmpty())) ? stateData : ExecutionStateDataFactory.getWith(stateData.executionMap, steps.length);
        return DataSupplierFactory.get(Executor.execute(
            ExecutionParametersDataFactory.getWithDefaultFunctionDataAndTwoCommandRange(Executor::execute),
            localStateData,
            steps
        ));
    }

    static <ReturnType> Function<ExecutionStateData, DataSupplier<ExecutionResultData<ReturnType>>> executeState(IGetMessage stepMessage, DataSupplier<?>... steps) {
        return stateData -> execute(stepMessage, stateData, steps);
    }

    static <ReturnType> Function<ExecutionStateData, DataSupplier<ExecutionResultData<ReturnType>>> executeState(String message, DataSupplier<?>... steps) {
        return stateData -> execute(message, stateData, steps);
    }

    static <ReturnType> Function<ExecutionStateData, DataSupplier<ExecutionResultData<ReturnType>>> executeState(QuadFunction<ExecutionStateData, String, Integer, Integer, String> messageHandler, DataSupplier<?>... steps) {
        return stateData -> execute(messageHandler, stateData, steps);
    }

    static <ReturnType> Function<ExecutionStateData, DataSupplier<ExecutionResultData<ReturnType>>> executeState(DataSupplier<?>... steps) {
        return stateData -> execute(stateData, steps);
    }

    static <ReturnType> Function<ExecutionStateData, DataSupplier<ExecutionResultData<ReturnType>>> conditionalSequenceState(TriPredicate<Data<?>, Integer, Integer> guard, DataSupplier<?> before, DataSupplier<?> after) {
        return stateData -> conditionalSequence(guard, stateData, before, after);
    }

    static <T, U, ReturnType> Function<ExecutionStateData, DataSupplier<ExecutionResultData<ReturnType>>> conditionalSequenceState(DataSupplier<T> before, DataSupplier<U> after, Class<ReturnType> clazz) {
        return stateData -> conditionalSequence(stateData, before, after, clazz);
    }

    static Data<Boolean> execute(int duration, DataSupplier<?>... steps) {
        final var nameof = "execute";
        if (NullableFunctions.isNull(steps) || (steps.length < 2) || (steps.length > 3) || (duration < 300)) {
            throw new ArgumentNullException("x");
        }

        final var tasks = TaskUtilities.getTaskListWithTimeouts(duration, steps);
        final var startTime = WaitConstants.CLOCK.instant();
        final var all = CompletableFutureExtensions.allOfTerminateOnFailureTimed(duration, TaskUtilities.getTaskArray(tasks));
        final var result = TaskExceptionHandlers.futureHandler(all);
        final var data = StepExecutorFormatters.getExecuteAllParallelTimedMessageData(tasks, all, result, startTime, WaitConstants.CLOCK.instant(), duration);
        final var status = data.status;
        return DataFactoryFunctions.getBoolean(status, nameof, data.message.message, result.exception);
    }

    static Data<Boolean> executeEndOnAnyResult(int duration, DataSupplier<?>... steps) {
        final var nameof = "executeEndOnAnyResult";
        if (NullableFunctions.isNull(steps) || (steps.length < 2) || (steps.length > 3) || (duration < 300)) {
            throw new ArgumentNullException("x");
        }

        final var tasks = TaskUtilities.getTaskListWithTimeouts(duration, steps);
        final var startTime = WaitConstants.CLOCK.instant();
        final var all = CompletableFutureExtensions.anyOfTerminateOnFailureTimed(duration, TaskUtilities.getTaskArray(tasks));
        final var result = TaskExceptionHandlers.futureHandler(all);
        final var data = StepExecutorFormatters.getExecuteAnyParallelTimedMessageData(tasks, all, result, startTime, WaitConstants.CLOCK.instant(), duration);
        final var status = data.status;
        return DataFactoryFunctions.getBoolean(status, nameof, data.message.message, result.exception);
    }
}

package com.github.karsaii.core.namespaces.executor.step;

import com.github.karsaii.core.constants.CoreConstants;
import com.github.karsaii.core.constants.ExecutorConstants;
import com.github.karsaii.core.constants.wait.WaitConstants;
import com.github.karsaii.core.exceptions.ArgumentNullException;
import com.github.karsaii.core.extensions.interfaces.functional.QuadFunction;
import com.github.karsaii.core.extensions.interfaces.functional.TriPredicate;
import com.github.karsaii.core.extensions.interfaces.functional.boilers.DataSupplier;
import com.github.karsaii.core.extensions.interfaces.functional.boilers.IGetMessage;
import com.github.karsaii.core.extensions.namespaces.NullableFunctions;
import com.github.karsaii.core.namespaces.DataFactoryFunctions;
import com.github.karsaii.core.namespaces.DataSupplierExecutionFunctions;
import com.github.karsaii.core.namespaces.ExceptionHandlers;
import com.github.karsaii.core.namespaces.factories.DataSupplierFactory;
import com.github.karsaii.core.namespaces.executor.ExecutorFunctionDataFactory;
import com.github.karsaii.core.namespaces.executor.ExecutionParametersDataFactory;
import com.github.karsaii.core.namespaces.executor.ExecutionResultDataFactory;
import com.github.karsaii.core.namespaces.executor.ExecutionStateDataFactory;
import com.github.karsaii.core.namespaces.executor.ExecutionStepsDataFactory;
import com.github.karsaii.core.namespaces.executor.Executor;
import com.github.karsaii.core.namespaces.formatter.executor.StepExecutorFormatters;
import com.github.karsaii.core.namespaces.predicates.DataPredicates;
import com.github.karsaii.core.namespaces.task.TaskUtilities;
import com.github.karsaii.core.records.Data;
import com.github.karsaii.core.records.SimpleMessageData;
import com.github.karsaii.core.records.executor.ExecutionParametersData;
import com.github.karsaii.core.records.executor.ExecutionResultData;
import com.github.karsaii.core.records.executor.ExecutionStateData;
import com.github.karsaii.core.records.executor.ExecutionStepsData;
import com.github.karsaii.core.constants.validators.CoreFormatterConstants;
import com.github.karsaii.core.namespaces.validators.CoreFormatter;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static com.github.karsaii.core.extensions.namespaces.NullableFunctions.isNotNull;

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
        final var negative = DataFactoryFunctions.getInvalidWithNameAndMessage(ExecutionResultDataFactory.getWithDefaultState(negativeReturnObject), "execute", CoreFormatterConstants.EMPTY);
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
        final var negative = DataFactoryFunctions.getInvalidWithNameAndMessage(negativeReturnObject, "execute", CoreFormatterConstants.EMPTY);
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
        final var localStateData = (isNotNull(stateData.indices) && !stateData.indices.isEmpty()) ? stateData : ExecutionStateDataFactory.getWith(stateData.executionMap, steps.length);
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
        final var localStateData = (isNotNull(stateData) && isNotNull(stateData.indices) && !stateData.indices.isEmpty()) ? stateData : ExecutionStateDataFactory.getWith(stateData.executionMap, steps.length);
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
        final var localStateData = (isNotNull(stateData.indices) && !stateData.indices.isEmpty()) ? stateData : ExecutionStateDataFactory.getWith(stateData.executionMap, steps.length);
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
        final var localStateData = (isNotNull(stateData.indices) && !stateData.indices.isEmpty()) ? stateData : ExecutionStateDataFactory.getWith(stateData.executionMap, steps.length);
        return DataSupplierFactory.get(Executor.execute(ExecutionParametersDataFactory.getWithDefaultFunctionDataAndDefaultRange(Executor::execute), localStateData, steps));
    }


    static <ReturnType> DataSupplier<ExecutionResultData<ReturnType>> conditionalSequence(TriPredicate<Data<?>, Integer, Integer> guard, ExecutionStateData stateData, DataSupplier<?> before, DataSupplier<?> after) {
        final DataSupplier<?>[] steps = Arrays.asList(before, after).toArray(new DataSupplier<?>[0]);
        final var localStateData = (isNotNull(stateData.indices) && !stateData.indices.isEmpty()) ? stateData : ExecutionStateDataFactory.getWith(stateData.executionMap, steps.length);
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
        final var localStateData = (isNotNull(stateData.indices) && !stateData.indices.isEmpty()) ? stateData : ExecutionStateDataFactory.getWith(stateData.executionMap, steps.length);
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
        if (NullableFunctions.isNull(steps) || (steps.length < 2) || (steps.length > 3) || (duration < 300)) {
            throw new ArgumentNullException("x");
        }

        final var startTime = WaitConstants.CLOCK.instant();
        final var tasks = TaskUtilities.getTaskList(steps);
        final var all = CompletableFuture.allOf(TaskUtilities.getTaskArray(tasks)).orTimeout(duration, TimeUnit.MILLISECONDS);
        final var result = ExceptionHandlers.futureHandler(all);
        final var stopTime = startTime.plus(duration, TimeUnit.MILLISECONDS.toChronoUnit());
        if (!all.isDone() || DataPredicates.isInvalidOrFalse(result)) {
            return DataFactoryFunctions.getBoolean(false, "reduceTasks", result.message.toString(), result.exception);
        }

        final var data = StepExecutorFormatters.getExecuteParallelTimedMessageData(tasks, startTime, stopTime);
        final var status = data.status;
        return DataFactoryFunctions.getWithNameAndMessage(status, status, "reduceTasks", data.message.message, result.exception);
    }




}

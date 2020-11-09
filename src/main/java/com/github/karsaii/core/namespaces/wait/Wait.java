package com.github.karsaii.core.namespaces.wait;

import com.github.karsaii.core.constants.CoreConstants;
import com.github.karsaii.core.constants.CoreDataConstants;
import com.github.karsaii.core.constants.validators.CoreFormatterConstants;
import com.github.karsaii.core.constants.wait.WaitConstants;
import com.github.karsaii.core.constants.wait.WaitFormatterConstants;
import com.github.karsaii.core.exceptions.ArgumentNullException;
import com.github.karsaii.core.exceptions.WaitTimeoutException;
import com.github.karsaii.core.extensions.interfaces.functional.boilers.DataSupplier;
import com.github.karsaii.core.extensions.namespaces.NullableFunctions;
import com.github.karsaii.core.extensions.namespaces.CoreUtilities;
import com.github.karsaii.core.namespaces.ExceptionHandlers;
import com.github.karsaii.core.namespaces.factories.wait.WaitDataFactory;
import com.github.karsaii.core.namespaces.predicates.DataPredicates;
import com.github.karsaii.core.namespaces.DataFactoryFunctions;
import com.github.karsaii.core.namespaces.executor.ExecutionStateDataFactory;
import com.github.karsaii.core.namespaces.validators.CoreFormatter;
import com.github.karsaii.core.namespaces.validators.WaitValidators;
import com.github.karsaii.core.namespaces.validators.wait.WaitDataValidators;
import com.github.karsaii.core.records.Data;
import com.github.karsaii.core.records.executor.ExecutionResultData;
import com.github.karsaii.core.records.executor.ExecutionStateData;
import com.github.karsaii.core.records.wait.tasks.repeat.WaitRepeatTask;
import com.github.karsaii.core.records.wait.tasks.regular.WaitTask;
import com.github.karsaii.core.records.wait.tasks.common.WaitTaskStateData;
import com.github.karsaii.core.records.wait.WaitData;
import com.github.karsaii.core.records.wait.WaitTimeData;

import java.time.Duration;
import java.util.ArrayList;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static com.github.karsaii.core.extensions.namespaces.NullableFunctions.isNotNull;
import static com.github.karsaii.core.namespaces.DataFunctions.getMessageFromData;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public interface Wait {
    static Data<Boolean> reduceTasks(int duration, DataSupplier<?>... steps) {
        if (NullableFunctions.isNull(steps) || (steps.length < 1) || (steps.length > 3) || (duration < 300)) {
            throw new ArgumentNullException("x");
        }

        final var tasks = new ArrayList<CompletableFuture<? extends Data<?>>>();
        final var startTime = WaitConstants.CLOCK.instant();
        var index = 0;
        final var length = steps.length;
        for(; index < length; ++index) {
            tasks.add(CompletableFuture.supplyAsync(steps[index].getSupplier()));
        }

        final var all = CompletableFuture.allOf(tasks.toArray(new CompletableFuture<?>[0])).orTimeout(duration, TimeUnit.MILLISECONDS);
        final var result = ExceptionHandlers.futureHandler(all);
        final var stopTime = startTime.plus(duration, TimeUnit.MILLISECONDS.toChronoUnit());
        if (!all.isDone() || DataPredicates.isInvalidOrFalse(result)) {
            return DataFactoryFunctions.getBoolean(false, "reduceTasks", result.message.toString(), result.exception);
        }
        index = 0;
        Data<?> current;
        for (; index < length; ++index) {
            current = ExceptionHandlers.futureHandler(tasks.get(index));
            if (DataPredicates.isInvalidOrFalse(current)) {
                break;
            }
        }

        final var status = index == length;
        return DataFactoryFunctions.getWithNameAndMessage(status, status, "reduceTasks", "reduceTasks: message", result.exception);
    }

    private static void runVoidTaskCore(WaitTask<Void, Void, Void> task) {
        if (NullableFunctions.isNotNull(task.commonData.function)) {
            task.commonData.function.apply(null);
        }
        task.stateData = new WaitTaskStateData<>(
                DataFactoryFunctions.replaceMessage(CoreDataConstants.VOID_TASK_RAN_SUCCESSFULLY, task.stateData.data.message.nameof, task.stateData.data.message.message),
                task.stateData.dependency
        );
        task.scheduler.shutdown();
    }

    private static <T, V> void runTaskCore(WaitTask<T, V, V> task) {
        final var commonData = task.commonData;
        final var result = commonData.function.apply(task.stateData.dependency);
        if (commonData.exitCondition.test(result)) {
            task.stateData = new WaitTaskStateData<>(
                DataFactoryFunctions.getValidWithNameAndMessage(result, "runTask", "Task executed successfully" + CoreFormatterConstants.END_LINE),
                task.stateData.dependency
            );
            task.scheduler.shutdown();
        }
    }

    private static <T, V> void runTaskCore(WaitRepeatTask<V> task) {
        final var commonData = task.commonData;
        final var result = commonData.function.apply(task.stateData.dependency).get();
        if (commonData.exitCondition.test(result)) {
            task.stateData = new WaitTaskStateData<>(
                DataFactoryFunctions.getValidWithNameAndMessage(result, "runTask", "Repeat task executed successfully" + CoreFormatterConstants.END_LINE),
                result.object.stateData
            );
            task.scheduler.shutdown();
        }
    }

    private static Runnable runVoidTask(WaitTask<Void, Void, Void> task) {
        return () -> runVoidTaskCore(task);
    }

    private static <T, V> Runnable runTask(WaitTask<T, V, V> task) {
        return () -> runTaskCore(task);
    }

    private static <T, U, V> Runnable runTask(WaitRepeatTask<V> task) {
        return () -> runTaskCore(task);
    }

    private static Data<Void> sleepCore(Runnable executable, WaitTask<Void, Void, Void> task, Duration duration) {
        var exception = CoreConstants.EXCEPTION;
        UnaryOperator<String> formatter = null;
        try {
            task.scheduler.schedule(executable, duration.toMillis(), TimeUnit.MILLISECONDS).get();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            formatter = CoreFormatter::getWaitInterruptMessage;
            exception = ex;
        }  catch (CancellationException ex) {
            exception = ex;
            if (CoreUtilities.isFalse(task.stateData.data.status)) {
                formatter = CoreFormatter::getWaitCancellationWithoutResultMessage;
            }
        } catch (ExecutionException ex) {
            formatter = CoreFormatter::getWaitExpectedExceptionMessage;
            exception = ex;
        }

        final var message =  NullableFunctions.isNotNull(formatter) ? formatter.apply(exception.getMessage()) : WaitFormatterConstants.TASK_SUCCESSFULLY_ENDED;
        return DataFactoryFunctions.getWithNameAndMessage(task.stateData.data.object, task.stateData.data.status, "sleep", message, exception);
    }

    private static <T, U, V> Data<V> untilTimeoutCore(Runnable executable, WaitTask<T, U, V> task, WaitTimeData data) {
        var exception = CoreConstants.EXCEPTION;
        UnaryOperator<String> formatter = null;
        try {
            task.scheduler.scheduleWithFixedDelay(executable, 0, data.interval.toMillis(), TimeUnit.MILLISECONDS).get(data.duration.toMillis(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            formatter = CoreFormatter::getWaitInterruptMessage;
            exception = ex;
        }  catch (CancellationException ex) {
            exception = ex;
            if (CoreUtilities.isFalse(task.stateData.data.status)) {
                formatter = CoreFormatter::getWaitCancellationWithoutResultMessage;
            }
        } catch (ExecutionException | TimeoutException ex) {
            formatter = CoreFormatter::getWaitExpectedExceptionMessage;
            exception = ex;
        }

        final var message =  NullableFunctions.isNotNull(formatter) ? formatter.apply(exception.getMessage()) : WaitFormatterConstants.TASK_SUCCESSFULLY_ENDED;
        return DataFactoryFunctions.getWithNameAndMessage(task.stateData.data.object, task.stateData.data.status, "untilTimeout", message, exception);
    }

    private static Data<Void> sleep(Function<WaitTask<Void, Void, Void>, Runnable> runner, WaitTask<Void, Void, Void> task, Duration duration) {
        return sleepCore(runner.apply(task), task, duration);
    }

    private static <T, U, V> Data<V> untilTimeout(Function<WaitTask<T, U, V>, Runnable> runner, WaitTask<T, U, V> task, WaitTimeData timeData) {
        return untilTimeoutCore(runner.apply(task), task, timeData);
    }

    private static <T, U, V> Data<Data<ExecutionResultData<V>>> repeatUntilTimeout(Function<WaitRepeatTask<V>, Runnable> runner, WaitRepeatTask<V> task, WaitTimeData timeData) {
        return untilTimeoutCore(runner.apply(task), task, timeData);
    }

    private static Supplier<Data<Void>> sleepSupplier(Function<WaitTask<Void, Void, Void>, Runnable> runner, WaitTask<Void, Void, Void> task, Duration duration) {
        return () -> sleep(runner, task, duration);
    }

    private static <T, U, V> Supplier<Data<V>> untilTimeoutSupplier(Function<WaitTask<T, U, V>, Runnable> runner, WaitTask<T, U, V> task, WaitTimeData timeData) {
        return () -> untilTimeout(runner, task, timeData);
    }

    private static <T, U, V> Supplier<Data<Data<ExecutionResultData<V>>>> repeatUntilTimeoutSupplier(Function<WaitRepeatTask<V>, Runnable> runner, WaitRepeatTask<V> task, WaitTimeData timeData) {
        return () -> repeatUntilTimeout(runner, task, timeData);
    }

    private static void sleepCoreCore(Supplier<Data<Void>> supplier, WaitData<Void, Void, Void> waitData) {
        final var start = waitData.timeData.clock.instant();
        final var result = supplier.get();
        final var end = waitData.timeData.clock.instant();
        if (CoreUtilities.isTrue(result.status)) {
            return;
        }

        final var conditionMessage = result.message.toString();
        var message = "";
        if (CoreUtilities.isNotEqual(result.message.message, WaitFormatterConstants.TASK_SUCCESSFULLY_ENDED)) {
            message = CoreFormatter.getWaitErrorMessage(conditionMessage, waitData.timeData, start, end);
        }

        throw new WaitTimeoutException(message + CoreFormatterConstants.END_LINE + conditionMessage, result.exception);
    }

    private static <T, U, V> V coreCore(Supplier<Data<V>> supplier, WaitData<T, U, V> waitData) {
        final var start = waitData.timeData.clock.instant();
        final var result = supplier.get();
        final var end = waitData.timeData.clock.instant();
        if (CoreUtilities.isTrue(result.status)) {
            return result.object;
        }

        final var conditionMessage = isNotNull(result.object) ? getMessageFromData(result.object) : waitData.conditionMessage;
        var message = "";
        if (CoreUtilities.isNotEqual(result.message.message, WaitFormatterConstants.TASK_SUCCESSFULLY_ENDED)) {
            message = CoreFormatter.getWaitErrorMessage(conditionMessage, waitData.timeData, start, end);
        }

        throw new WaitTimeoutException(message + CoreFormatterConstants.END_LINE + conditionMessage, result.exception);
    }

    private static void sleep(WaitData<Void, Void, Void> waitData) {
        final var errorMessage = WaitDataValidators.isValidSleepData(waitData);
        if (isNotBlank(errorMessage)) {
            throw new ArgumentNullException(errorMessage);
        }

        sleepCoreCore(sleepSupplier(
            Wait::runVoidTask,
            new WaitTask<>(Executors.newSingleThreadScheduledExecutor(), waitData.taskData, new WaitTaskStateData<>(DataFactoryFunctions.getInvalidWithNameAndMessage(null, "sleep", "sleep"), null)),
            waitData.timeData.duration
        ), waitData);
    }

    private static <T, V> V core(T dependency, WaitData<T, V, V> data) {
        final var errorMessage = WaitValidators.isValidWaitParameters(dependency, data);
        if (isNotBlank(errorMessage)) {
            throw new ArgumentNullException(errorMessage);
        }

        return coreCore(untilTimeoutSupplier(
            Wait::runTask,
            new WaitTask<>(Executors.newSingleThreadScheduledExecutor(), data.taskData, new WaitTaskStateData<>(DataFactoryFunctions.getInvalidWithNameAndMessage(null, "core", "core"), dependency)),
            data.timeData
        ), data);
    }

    private static <ReturnType> Data<ExecutionResultData<ReturnType>> repeat(
        ExecutionStateData dependency,
        WaitData<ExecutionStateData, DataSupplier<ExecutionResultData<ReturnType>>, Data<ExecutionResultData<ReturnType>>> data
    ) {
        final var errorMessage = WaitValidators.isValidWaitParameters(dependency, data);
        if (isNotBlank(errorMessage)) {
            throw new ArgumentNullException(errorMessage);
        }

        return coreCore(repeatUntilTimeoutSupplier(
            Wait::runTask,
            new WaitRepeatTask<>(Executors.newSingleThreadScheduledExecutor(), data.taskData, new WaitTaskStateData<>(DataFactoryFunctions.getInvalidWithNameAndMessage(null, "repeat", "repeat"), dependency)),
            data.timeData
        ), data);
    }

    static void sleep(int duration) {
        sleep(WaitDataFactory.getWithSleepDuration(null, null, duration));
    }

    static <T, V> Function<T, V> core(WaitData<T, V, V> waitData) {
        return dependency -> core(dependency, waitData);
    }

    static <ReturnType> Function<ExecutionStateData, Data<ExecutionResultData<ReturnType>>> repeat(WaitData<ExecutionStateData, DataSupplier<ExecutionResultData<ReturnType>>, Data<ExecutionResultData<ReturnType>>> waitData) {
        return dependency -> repeat(dependency, waitData);
    }

    static Function<?, Void> sleepFunction(int timeout) {
        return any -> {
            sleep(timeout);
            return null;
        };
    }

    static <ReturnType> Data<ExecutionResultData<ReturnType>> repeatWithDefaultState(WaitData<ExecutionStateData, DataSupplier<ExecutionResultData<ReturnType>>, Data<ExecutionResultData<ReturnType>>> waitData) {
        return repeat(waitData).apply(ExecutionStateDataFactory.getWithDefaults());
    }
}

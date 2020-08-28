package com.github.karsaii.core.namespaces.wait;

import com.github.karsaii.core.exceptions.ArgumentNullException;
import com.github.karsaii.core.exceptions.WaitTimeoutException;
import com.github.karsaii.core.extensions.interfaces.functional.boilers.DataSupplier;
import com.github.karsaii.core.namespaces.executor.ExecutionStateDataFactory;
import com.github.karsaii.core.namespaces.validators.WaitValidators;
import com.github.karsaii.core.extensions.namespaces.CoreUtilities;
import com.github.karsaii.core.records.Data;
import com.github.karsaii.core.records.wait.WaitData;
import com.github.karsaii.core.records.executor.ExecutionResultData;
import com.github.karsaii.core.records.executor.ExecutionStateData;
import com.github.karsaii.core.constants.validators.CoreFormatterConstants;
import com.github.karsaii.core.namespaces.validators.CoreFormatter;

import java.util.function.Function;

import static com.github.karsaii.core.extensions.namespaces.NullableFunctions.isNotNull;
import static com.github.karsaii.core.namespaces.DataFunctions.getMessageFromData;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public interface Wait {
    private static <T, V> V core(T dependency, WaitData<T, V, V> waitData) {
        if (CoreUtilities.areAnyNull(dependency, waitData)) {
            throw new ArgumentNullException("Dependency or WaitData was wrong" + CoreFormatterConstants.END_LINE);
        }

        final var function = waitData.function;
        final var exitCondition = waitData.exitCondition;
        final var timeData = waitData.timeData;
        final var errorMessage = WaitValidators.validateUntilParameters(function, exitCondition, timeData);
        if (isNotBlank(errorMessage)) {
            throw new ArgumentNullException(errorMessage);
        }

        final var timeout = timeData.duration;
        final var interval = timeData.interval.toMillis();
        final var clock = timeData.clock;

        final var start = clock.instant();
        final var end = start.plus(timeout);
        var message = "";
        V value = null;
        try {
            for(; end.isAfter(clock.instant()); Thread.sleep(interval)) {
                value = function.apply(dependency);
                if (exitCondition.test(value)) {
                    return value;
                }
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            message = CoreFormatter.getWaitInterruptMessage(ex.getMessage());
        }

        final var conditionMessage = waitData.conditionMessage;
        if (isBlank(message)) {
            message = CoreFormatter.getWaitErrorMessage(
                    isNotNull(value) ? getMessageFromData(value) : conditionMessage, timeout.getSeconds(), interval
            ) + "End time: " + end + CoreFormatterConstants.END_LINE + "Start: " + start + CoreFormatterConstants.END_LINE;
        }

        throw new WaitTimeoutException(message + conditionMessage);
    }

    private static <ReturnType> Data<ExecutionResultData<ReturnType>> repeat(
        ExecutionStateData dependency,
        WaitData<ExecutionStateData, DataSupplier<ExecutionResultData<ReturnType>>, Data<ExecutionResultData<ReturnType>>> waitData
    ) {
        if (CoreUtilities.areAnyNull(dependency, waitData)) {
            throw new ArgumentNullException("Starting state or StepWaitData was wrong, " + CoreFormatterConstants.WAS_NULL);
        }

        final var function = waitData.function;
        final var exitCondition = waitData.exitCondition;
        final var timeData = waitData.timeData;
        final var errorMessage = WaitValidators.validateUntilParametersRepeat(function, exitCondition, timeData);
        if (isNotBlank(errorMessage)) {
            throw new ArgumentNullException(errorMessage);
        }

        final var timeout = timeData.duration;
        final var interval = timeData.interval.toMillis();
        final var clock = timeData.clock;

        final var start = clock.instant();
        final var end = start.plus(timeout);
        var message = "";
        Data<ExecutionResultData<ReturnType>> value = null;
        var state = dependency;
        try {
            for(; end.isAfter(clock.instant()); Thread.sleep(interval)) {
                value = function.apply(state).get();
                if (exitCondition.test(value)) {
                    return value;
                }

                state = value.object.stateData;
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            message = CoreFormatter.getWaitInterruptMessage(ex.getMessage());
        }

        final var conditionMessage = waitData.conditionMessage;
        if (isBlank(message)) {
            message = CoreFormatter.getWaitErrorMessage(
                    isNotNull(value) ? getMessageFromData(value) : conditionMessage, timeout.getSeconds(), interval
            ) + "End time: " + end + CoreFormatterConstants.END_LINE + "Start: " + start + CoreFormatterConstants.END_LINE;
        }

        throw new WaitTimeoutException(message + conditionMessage);
    }

    static <T, V> Function<T, V> core(WaitData<T, V, V> waitData) {
        return dependency -> core(dependency, waitData);
    }

    static <ReturnType> Function<ExecutionStateData, Data<ExecutionResultData<ReturnType>>> repeat(WaitData<ExecutionStateData, DataSupplier<ExecutionResultData<ReturnType>>, Data<ExecutionResultData<ReturnType>>> waitData) {
        return dependency -> repeat(dependency, waitData);
    }

    static <ReturnType> Data<ExecutionResultData<ReturnType>> repeatWithDefaultState(WaitData<ExecutionStateData, DataSupplier<ExecutionResultData<ReturnType>>, Data<ExecutionResultData<ReturnType>>> waitData) {
        return repeat(waitData).apply(ExecutionStateDataFactory.getWithDefaults());
    }
}

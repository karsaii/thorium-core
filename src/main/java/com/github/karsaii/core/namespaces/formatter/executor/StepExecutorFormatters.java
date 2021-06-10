package com.github.karsaii.core.namespaces.formatter.executor;

import com.github.karsaii.core.constants.validators.CoreFormatterConstants;
import com.github.karsaii.core.extensions.namespaces.CoreUtilities;
import com.github.karsaii.core.extensions.namespaces.predicates.BasicPredicates;
import com.github.karsaii.core.extensions.namespaces.predicates.SizablePredicates;
import com.github.karsaii.core.namespaces.DataFactoryFunctions;
import com.github.karsaii.core.namespaces.DataFunctions;
import com.github.karsaii.core.namespaces.exception.TaskExceptionHandlers;
import com.github.karsaii.core.namespaces.predicates.DataPredicates;
import com.github.karsaii.core.records.Data;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface StepExecutorFormatters {
    private static String getTaskExecutionTimeMessage(Instant startTime, Instant stopTime, int duration) {
        return (
            "\tExecution ran from time(\"" + startTime.toString() + "\") to (\"" + stopTime.toString() + "\")" + CoreFormatterConstants.END_LINE +
            "\tDuration(\"" + duration + "\" milliseconds), actually ran for " + ChronoUnit.MILLIS.between(startTime, stopTime) + " milliseconds" + CoreFormatterConstants.END_LINE
        );
    }

    private static String getTaskIndexedMessage(int index, String message) {
        return "\t" + index + ". task: " + message;
    }

    private static String getAmountFragmentMessage(int passed, int length) {
        var message = "";
        if (SizablePredicates.isSizeEqualTo(passed, length)) {
            message = "All(\"" + length + "\") tasks executed successfully";
        } else {
            if (BasicPredicates.isPositiveNonZero(passed)) {
                message = "Some(\"" + passed + "\") tasks executed successfully out of all(\"" + length + "\")";
            } else {
                message = "No tasks executed successfully";
            }
        }

        return message + CoreFormatterConstants.END_LINE;
    }

    static Data<Boolean> getExecuteAllParallelTimedMessageData(List<CompletableFuture<? extends Data<?>>> tasks, CompletableFuture<?> all, Data<Boolean> result, Instant startTime, Instant stopTime, int duration) {
        final var messageBuilder = new StringBuilder(getTaskExecutionTimeMessage(startTime, stopTime, duration));
        final var done = CoreUtilities.isFalse(
            CoreUtilities.isFalse(all.isDone()) ||
            DataPredicates.isInvalidOrFalse(result)
        );
        if (CoreUtilities.isFalse(done)) {
            final var messageData = result.message;
            messageBuilder.append("\t").append(messageData.formatter.apply(messageData.nameof, messageData.message));
        }

        final var length = tasks.size();
        var passed = length;

        Data<?> current;
        for (var index = 0; index < length; ++index) {
            current = TaskExceptionHandlers.futureDataHandler(tasks.get(index));
            if (DataPredicates.isInvalidOrFalse(current)) {
                --passed;
            }

            messageBuilder.append(getTaskIndexedMessage(index + 1, DataFunctions.getStatusMessageFromData(current).replaceAll("\\n\\t", "\n\t\t")));
        }

        return DataFactoryFunctions.getBoolean(done && SizablePredicates.isSizeEqualTo(passed, length), "getExecuteAllParallelTimedMessageData", getAmountFragmentMessage(passed, length)  + messageBuilder);
    }

    static Data<Boolean> getExecuteAnyParallelTimedMessageData(List<CompletableFuture<? extends Data<?>>> tasks, CompletableFuture<?> all, Data<Boolean> result, Instant startTime, Instant stopTime, int duration) {
        final var messageBuilder = new StringBuilder(getTaskExecutionTimeMessage(startTime, stopTime, duration));
        final var done = CoreUtilities.isFalse(
            CoreUtilities.isFalse(all.isDone()) ||
            DataPredicates.isInvalidOrFalse(result)
        );
        if (CoreUtilities.isFalse(done)) {
            final var messageData = result.message;
            messageBuilder.append("\t").append(messageData.formatter.apply(messageData.nameof, messageData.message));
        }

        final var length = tasks.size();
        var passed = length;

        Data<?> current;
        for (var index = 0; index < length; ++index) {
            current = TaskExceptionHandlers.futureDataHandler(tasks.get(index));
            if (DataPredicates.isInvalidOrFalse(current)) {
                --passed;
            }

            messageBuilder.append(getTaskIndexedMessage(index + 1, DataFunctions.getStatusMessageFromData(current).replaceAll("\\n\\t", "\n\t\t")));
        }

        return DataFactoryFunctions.getBoolean(done && BasicPredicates.isPositiveNonZero(passed), "getExecuteAllParallelTimedMessageData", getAmountFragmentMessage(passed, length)  + messageBuilder);
    }
}

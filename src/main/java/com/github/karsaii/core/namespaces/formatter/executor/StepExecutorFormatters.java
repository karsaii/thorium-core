package com.github.karsaii.core.namespaces.formatter.executor;

import com.github.karsaii.core.constants.validators.CoreFormatterConstants;
import com.github.karsaii.core.extensions.namespaces.predicates.SizablePredicates;
import com.github.karsaii.core.namespaces.DataFactoryFunctions;
import com.github.karsaii.core.namespaces.DataFunctions;
import com.github.karsaii.core.namespaces.exception.TaskExceptionHandlers;
import com.github.karsaii.core.namespaces.predicates.DataPredicates;
import com.github.karsaii.core.records.Data;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface StepExecutorFormatters {
    private static String getTaskExecutionTimeMessage(Instant startTime, Instant stopTime) {
        return "Execution ran from time(\"" + startTime.toString() + "\") to (\"" + stopTime.toString() + "\")" + CoreFormatterConstants.END_LINE;
    }

    private static String getTaskIndexedMessage(int index, String message) {
        return "\t" + index + ". task: " + message;
    }

    private static String getAmountFragmentMessage(int passed, int length) {
        var message = "";
        if (SizablePredicates.isSizeEqualTo(passed, length)) {
            message = "All(\"" + length + "\") tasks executed successfully";
        } else {
            if (passed > 0) {
                message = "Some(\"" + passed + "\") tasks executed successfully out of all(\"" + length + "\")";
            } else {
                message = "No tasks executed successfully";
            }
        }

        return message + CoreFormatterConstants.END_LINE;
    }

    static Data<Boolean> getExecuteParallelTimedMessageData(List<CompletableFuture<? extends Data<?>>> tasks, Instant startTime, Instant stopTime) {
        final var messageBuilder = new StringBuilder(getTaskExecutionTimeMessage(startTime, stopTime));
        final var length = tasks.size();
        var passed = length;
        var index = 0;

        Data<?> current;
        for (; index < length; ++index) {
            current = TaskExceptionHandlers.futureDataHandler(tasks.get(index));
            if (DataPredicates.isInvalidOrFalse(current)) {
                --passed;
            }

            messageBuilder.append(getTaskIndexedMessage(index + 1, DataFunctions.getStatusMessageFromData(current)));
        }

        return DataFactoryFunctions.getBoolean(
            SizablePredicates.isSizeEqualTo(passed, length),
            "getExecuteParallelTimedMessageData",
            "\n\nexecute: " + getAmountFragmentMessage(passed, length)  + messageBuilder.toString()
        );
    }
}

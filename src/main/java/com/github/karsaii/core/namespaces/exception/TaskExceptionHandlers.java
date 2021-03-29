package com.github.karsaii.core.namespaces.exception;

import com.github.karsaii.core.constants.CoreConstants;
import com.github.karsaii.core.constants.validators.CoreFormatterConstants;
import com.github.karsaii.core.extensions.namespaces.NullableFunctions;
import com.github.karsaii.core.namespaces.DataFactoryFunctions;
import com.github.karsaii.core.namespaces.DataFunctions;
import com.github.karsaii.core.namespaces.predicates.DataPredicates;
import com.github.karsaii.core.records.Data;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.github.karsaii.core.extensions.namespaces.CoreUtilities.isNonException;

public interface TaskExceptionHandlers {
    static <T> Data<Boolean> futureHandler(CompletableFuture<T> task) {
        final var nameof = "futureHandler";
        if (NullableFunctions.isNull(task)) {
            return DataFactoryFunctions.getInvalidBooleanWith(nameof, "Task parameter" + CoreFormatterConstants.WAS_NULL);
        }

        var exception = CoreConstants.EXCEPTION;
        try {
            task.get();
        } catch (CancellationException | InterruptedException | ExecutionException ex) {
            exception = ex;
        }

        final var status = isNonException(exception) && task.isDone();
        return DataFactoryFunctions.getBoolean(status, nameof, status ? CoreFormatterConstants.INVOCATION_SUCCESSFUL : CoreFormatterConstants.INVOCATION_EXCEPTION, exception);
    }

    static Data<Boolean> futureDataHandler(CompletableFuture<? extends Data<?>> task) {
        final var nameof = "futureDataHandler";
        if (NullableFunctions.isNull(task)) {
            return DataFactoryFunctions.getInvalidBooleanWith(nameof, "Task parameter" + CoreFormatterConstants.WAS_NULL);
        }

        var exception = CoreConstants.EXCEPTION;
        Data<?> data = null;
        try {
            data = task.get();
        } catch (CancellationException | InterruptedException | ExecutionException ex) {
            exception = ex;
        }

        final var status = isNonException(exception) && DataPredicates.isValidNonFalse(data);
        return DataFactoryFunctions.getBoolean(
            status,
            nameof,
            task.isDone() ? DataFunctions.getStatusMessageFromData(data) : CoreFormatterConstants.INVOCATION_EXCEPTION,
            exception
        );
    }
}

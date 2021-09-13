package com.neathorium.core.namespaces.exception;

import com.neathorium.core.constants.CoreConstants;
import com.neathorium.core.constants.validators.CoreFormatterConstants;
import com.neathorium.core.extensions.namespaces.NullableFunctions;
import com.neathorium.core.namespaces.DataFactoryFunctions;
import com.neathorium.core.namespaces.DataFunctions;
import com.neathorium.core.namespaces.predicates.DataPredicates;
import com.neathorium.core.records.Data;
import com.neathorium.core.extensions.namespaces.CoreUtilities;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

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

        final var status = CoreUtilities.isNonException(exception) && task.isDone();
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

        final var isNotBlank = NullableFunctions.isNotNull(data) && NullableFunctions.isNotNull(data.message) && isNotBlank(data.message.nameof);
        return DataFactoryFunctions.getBoolean(
            CoreUtilities.isNonException(exception) && DataPredicates.isValidNonFalse(data),
            isNotBlank ? data.message.nameof : nameof,
            task.isDone() ? DataFunctions.getStatusMessageFromData(data) : CoreFormatterConstants.INVOCATION_EXCEPTION,
            exception
        );
    }
}

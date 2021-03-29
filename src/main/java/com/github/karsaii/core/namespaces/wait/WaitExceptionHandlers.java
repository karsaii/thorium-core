package com.github.karsaii.core.namespaces.wait;

import com.github.karsaii.core.constants.CoreConstants;
import com.github.karsaii.core.constants.wait.WaitFormatterConstants;
import com.github.karsaii.core.exceptions.WrappedExecutionException;
import com.github.karsaii.core.exceptions.WrappedThreadInterruptedException;
import com.github.karsaii.core.exceptions.WrappedTimeoutException;
import com.github.karsaii.core.extensions.namespaces.CoreUtilities;
import com.github.karsaii.core.extensions.namespaces.NullableFunctions;
import com.github.karsaii.core.namespaces.DataFactoryFunctions;
import com.github.karsaii.core.namespaces.validators.CoreFormatter;
import com.github.karsaii.core.records.Data;
import com.github.karsaii.core.records.wait.tasks.regular.WaitTask;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.function.UnaryOperator;

public interface WaitExceptionHandlers {
    static Data<UnaryOperator<String>> futureInformationHandler(WaitTask<?, ?, ?> task, Runnable runnable) {
        var exception = CoreConstants.EXCEPTION;
        UnaryOperator<String> formatter = null;
        try {
            runnable.run();
        } catch (WrappedThreadInterruptedException ex) {
            Thread.currentThread().interrupt();
            exception = new InterruptedException(ex.getMessage());
            formatter = CoreFormatter::getWaitInterruptMessage;
        } catch (CancellationException ex) {
            exception = ex;
            if (CoreUtilities.isFalse(task.stateData.data.status)) {
                formatter = CoreFormatter::getWaitCancellationWithoutResultMessage;
            }
        } catch (WrappedExecutionException ex) {
            exception = new ExecutionException(ex.getMessage(), ex.getCause());
            formatter = CoreFormatter::getWaitExpectedExceptionMessage;
        } catch (WrappedTimeoutException ex) {
            exception = new TimeoutException(ex.getMessage());
            formatter = CoreFormatter::getWaitExpectedExceptionMessage;
        }

        final var message =  NullableFunctions.isNotNull(formatter) ? formatter.apply(exception.getMessage()) : WaitFormatterConstants.TASK_SUCCESSFULLY_ENDED;
        return DataFactoryFunctions.getWith(formatter, task.stateData.data.status, "futureInformationHandler", message, exception);
    }
}

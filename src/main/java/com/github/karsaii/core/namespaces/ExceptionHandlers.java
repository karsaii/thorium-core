package com.github.karsaii.core.namespaces;

import com.github.karsaii.core.extensions.namespaces.NullableFunctions;
import com.github.karsaii.core.records.Data;
import com.github.karsaii.core.records.HandleResultData;
import com.github.karsaii.core.constants.validators.CoreFormatterConstants;
import com.github.karsaii.core.constants.CoreConstants;
import com.github.karsaii.core.namespaces.validators.HandlerResultDataValidator;
import com.github.karsaii.core.records.clipboard.ClipboardData;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.github.karsaii.core.extensions.namespaces.CoreUtilities.isNonException;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public interface ExceptionHandlers {
    static <CastParameterType, ReturnType> Data<ReturnType> classCastHandler(HandleResultData<CastParameterType, ReturnType> data) {
        final var nameof = "classCastHandler";
        final var errorMessage = HandlerResultDataValidator.isInvalidHandlerResultDataMessage(data);
        if (isNotBlank(errorMessage)) {
            return DataFactoryFunctions.getInvalidWithNameAndMessage(null, nameof, errorMessage);
        }

        var exception = CoreConstants.EXCEPTION;
        var result = data.defaultValue;
        try {
            result = data.caster.apply(data.parameter);
        } catch (ClassCastException ex) {
            exception = ex;
        }

        final var status = isNonException(exception);
        return DataFactoryFunctions.getWithMessage(result, status, status ? CoreFormatterConstants.INVOCATION_SUCCESSFUL : CoreFormatterConstants.INVOCATION_EXCEPTION, exception);
    }

    static <T> Data<Boolean> futureHandler(CompletableFuture<T> task) {
        final var nameof = "futureHandler";
        if (NullableFunctions.isNull(task)) {
            return DataFactoryFunctions.getInvalidBooleanWithNameAndMessage(nameof, "Task parameter" + CoreFormatterConstants.WAS_NULL);
        }

        var exception = CoreConstants.EXCEPTION;
        try {
            task.get();
        } catch (
            CancellationException | InterruptedException | ExecutionException ex
        ) {
            exception = ex;
        }

        final var status = isNonException(exception) && task.isDone();
        return DataFactoryFunctions.getBoolean(status, nameof, status ? CoreFormatterConstants.INVOCATION_SUCCESSFUL : CoreFormatterConstants.INVOCATION_EXCEPTION, exception);
    }

    static Data<Object> transferHandler(ClipboardData<?> data) {
        var result = CoreConstants.STOCK_OBJECT;
        var exception = CoreConstants.EXCEPTION;
        try {
            result = data.clipboard.getContents(data.owner).getTransferData(data.flavor);
        } catch (UnsupportedFlavorException | IOException ex) {
            exception = ex;
        }

        final var status = isNonException(exception);
        return DataFactoryFunctions.getWithNameAndMessage(result, status, "transferHandler", "Copying from clipboard was" + (status ? "" : "n't") + CoreFormatterConstants.SUCCESSFUL, exception);
    }

    static <T> Data<Boolean> setContentsHandler(Clipboard clipboard, StringSelection message) {
        var exception = CoreConstants.EXCEPTION;
        try {
            clipboard.setContents(message, null);
        } catch (IllegalStateException ex) {
            exception = ex;
        }

        final var status = isNonException(exception);
        return DataFactoryFunctions.getBoolean(status, "setContentsHandler", "Copying to clipboard was" + (status ? "" : "n't") + CoreFormatterConstants.SUCCESSFUL, exception);
    }
}

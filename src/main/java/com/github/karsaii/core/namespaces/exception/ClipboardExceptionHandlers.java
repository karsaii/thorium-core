package com.github.karsaii.core.namespaces.exception;

import com.github.karsaii.core.constants.CoreConstants;
import com.github.karsaii.core.constants.validators.CoreFormatterConstants;
import com.github.karsaii.core.namespaces.DataFactoryFunctions;
import com.github.karsaii.core.records.Data;
import com.github.karsaii.core.records.clipboard.ClipboardData;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import static com.github.karsaii.core.extensions.namespaces.CoreUtilities.isNonException;

public interface ClipboardExceptionHandlers {
    static Data<Object> transferHandler(ClipboardData<?> data) {
        var result = CoreConstants.STOCK_OBJECT;
        var exception = CoreConstants.EXCEPTION;
        try {
            result = data.clipboard.getContents(data.owner).getTransferData(data.flavor);
        } catch (UnsupportedFlavorException | IOException ex) {
            exception = ex;
        }

        final var status = isNonException(exception);
        return DataFactoryFunctions.getWith(result, status, "transferHandler", "Copying from clipboard was" + (status ? "" : "n't") + CoreFormatterConstants.SUCCESSFUL, exception);
    }

    static Data<Boolean> setContentsHandler(Clipboard clipboard, StringSelection message) {
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

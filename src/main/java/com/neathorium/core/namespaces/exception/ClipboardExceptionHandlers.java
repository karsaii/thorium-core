package com.neathorium.core.namespaces.exception;

import com.neathorium.core.constants.CoreConstants;
import com.neathorium.core.constants.validators.CoreFormatterConstants;
import com.neathorium.core.namespaces.DataFactoryFunctions;
import com.neathorium.core.records.Data;
import com.neathorium.core.records.clipboard.ClipboardData;
import com.neathorium.core.extensions.namespaces.CoreUtilities;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public interface ClipboardExceptionHandlers {
    static Data<Object> transferHandler(ClipboardData<?> data) {
        var result = CoreConstants.STOCK_OBJECT;
        var exception = CoreConstants.EXCEPTION;
        try {
            result = data.clipboard.getContents(data.owner).getTransferData(data.flavor);
        } catch (UnsupportedFlavorException | IOException ex) {
            exception = ex;
        }

        final var status = CoreUtilities.isNonException(exception);
        return DataFactoryFunctions.getWith(result, status, "transferHandler", "Copying from com.neathorium.core.clipboard was" + (status ? "" : "n't") + CoreFormatterConstants.SUCCESSFUL, exception);
    }

    static Data<Boolean> setContentsHandler(Clipboard clipboard, StringSelection message) {
        var exception = CoreConstants.EXCEPTION;
        try {
            clipboard.setContents(message, null);
        } catch (IllegalStateException ex) {
            exception = ex;
        }

        final var status = CoreUtilities.isNonException(exception);
        return DataFactoryFunctions.getBoolean(status, "setContentsHandler", "Copying to com.neathorium.core.clipboard was" + (status ? "" : "n't") + CoreFormatterConstants.SUCCESSFUL, exception);
    }
}

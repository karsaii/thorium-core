package com.github.karsaii.core.namespaces.clipboard;

import com.github.karsaii.core.constants.CoreConstants;
import com.github.karsaii.core.constants.CoreDataConstants;
import com.github.karsaii.core.constants.clipboard.ClipboardConstants;
import com.github.karsaii.core.constants.validators.CoreFormatterConstants;
import com.github.karsaii.core.extensions.namespaces.CoreUtilities;
import com.github.karsaii.core.namespaces.DataFactoryFunctions;
import com.github.karsaii.core.records.Data;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.function.Function;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public interface ClipboardFunctions {
    private static Data<Boolean> copyToClipboardCore(String message) {
        final var selection = new StringSelection(message);

        final var clipboard = ClipboardConstants.CLIPBOARD;
        clipboard.setContents(selection, null);
        final var x = clipboard.getContents(null);
        var lMessage = CoreFormatterConstants.EMPTY;
        var exception = CoreConstants.EXCEPTION;
        if (x.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            try {
                lMessage = (String) x.getTransferData(DataFlavor.stringFlavor);
            } catch (UnsupportedFlavorException | IOException ex) {
                exception = ex;
            }
        }

        final var status = isNotBlank(lMessage) && CoreUtilities.isNonException(exception);
        return DataFactoryFunctions.getWithNameAndMessage(status, status, "copyToClipBoard", "Copying(\"" + message + "\") to clipboard was " + (status ? "" : "un") + "successful" + CoreFormatterConstants.END_LINE, exception);
    }

    static Function<String, Data<Boolean>> copyToClipboard() {
        return ClipboardFunctions::copyToClipboardCore;
    }

    static Data<Boolean> copyToClipboard(String message) {
        return copyToClipboard().apply(message);
    }
}

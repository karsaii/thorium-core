package com.github.karsaii.core.namespaces.clipboard;

import com.github.karsaii.core.constants.CoreDataConstants;
import com.github.karsaii.core.constants.clipboard.ClipboardConstants;
import com.github.karsaii.core.constants.validators.CoreFormatterConstants;
import com.github.karsaii.core.extensions.namespaces.CoreUtilities;
import com.github.karsaii.core.extensions.namespaces.NullableFunctions;
import com.github.karsaii.core.namespaces.DataFactoryFunctions;
import com.github.karsaii.core.namespaces.ExceptionHandlers;
import com.github.karsaii.core.namespaces.predicates.DataPredicates;
import com.github.karsaii.core.namespaces.validators.CoreFormatter;
import com.github.karsaii.core.namespaces.validators.clipboard.ClipboardValidators;
import com.github.karsaii.core.records.Data;
import com.github.karsaii.core.records.HandleResultData;
import com.github.karsaii.core.records.clipboard.ClipboardData;

import java.awt.datatransfer.StringSelection;
import java.util.function.Function;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public interface ClipboardFunctions {
    private static Data<Boolean> copyToClipboardCore(ClipboardData<String> data, String message) {
        final var nameof = "copyToClipboard";
        final var errorMessage = ClipboardValidators.isValidClipboardData(data);
        if (isNotBlank(errorMessage)) {
            return DataFactoryFunctions.getInvalidBooleanWithNameAndMessage(nameof, errorMessage);
        }

        final var setResult = ExceptionHandlers.setContentsHandler(data.clipboard, new StringSelection(message));
        if (DataPredicates.isInvalidOrFalse(setResult)) {
            return DataFactoryFunctions.replaceMessage(setResult, nameof, setResult.message.toString());
        }

        final var getResult = getFromClipboardCore(data);
        final var exception = getResult.exception;
        final var status = isNotBlank(getResult.object) && CoreUtilities.isNonException(exception);
        return DataFactoryFunctions.getWithNameAndMessage(status, status, nameof, "Copying(\"" + message + "\") to clipboard was " + (status ? "" : "un") + "successful" + CoreFormatterConstants.END_LINE, exception);
    }

    private static Data<String> getFromClipboardCore(ClipboardData<String> data) {
        final var nameof = "getFromClipboard";
        final var getResult = ExceptionHandlers.transferHandler(data);
        if (DataPredicates.isInvalidOrFalse(getResult)) {
            return DataFactoryFunctions.getWithNameAndMessage(CoreFormatterConstants.EMPTY, getResult.status, nameof, getResult.message.toString(), getResult.exception);
        }

        final var castResult = ExceptionHandlers.classCastHandler(new HandleResultData<>(data.castData.caster, getResult.object, data.castData.defaultValue));
        return DataFactoryFunctions.replaceName(castResult, nameof);
    }

    static Function<String, Data<Boolean>> copyToClipboard() {
        return message -> NullableFunctions.isNotNull(message) ? copyToClipboardCore(ClipboardConstants.CLIPBOARD_DATA, message) : CoreDataConstants.NULL_BOOLEAN;
    }

    static Data<Boolean> copyToClipboard(String message) {
        return copyToClipboard().apply(message);
    }
}

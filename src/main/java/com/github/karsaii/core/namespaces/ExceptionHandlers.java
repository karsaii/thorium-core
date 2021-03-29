package com.github.karsaii.core.namespaces;

import com.github.karsaii.core.records.Data;
import com.github.karsaii.core.records.HandleResultData;
import com.github.karsaii.core.constants.validators.CoreFormatterConstants;
import com.github.karsaii.core.constants.CoreConstants;
import com.github.karsaii.core.namespaces.validators.HandlerResultDataValidator;

import static com.github.karsaii.core.extensions.namespaces.CoreUtilities.isNonException;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public interface ExceptionHandlers {
    static <CastParameterType, ReturnType> Data<ReturnType> classCastHandler(HandleResultData<CastParameterType, ReturnType> data) {
        final var nameof = "classCastHandler";
        final var errorMessage = HandlerResultDataValidator.isInvalidHandlerResultDataMessage(data);
        if (isNotBlank(errorMessage)) {
            return DataFactoryFunctions.getInvalidWith(null, nameof, errorMessage);
        }

        var exception = CoreConstants.EXCEPTION;
        var result = data.defaultValue;
        try {
            result = data.caster.apply(data.parameter);
        } catch (ClassCastException ex) {
            exception = ex;
        }

        final var status = isNonException(exception);
        return DataFactoryFunctions.getWith(result, status, status ? CoreFormatterConstants.INVOCATION_SUCCESSFUL : CoreFormatterConstants.INVOCATION_EXCEPTION, exception);
    }
}

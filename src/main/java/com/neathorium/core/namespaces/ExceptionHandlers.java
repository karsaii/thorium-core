package com.neathorium.core.namespaces;

import com.neathorium.core.records.Data;
import com.neathorium.core.records.HandleResultData;
import com.neathorium.core.constants.validators.CoreFormatterConstants;
import com.neathorium.core.constants.CoreConstants;
import com.neathorium.core.namespaces.validators.HandlerResultDataValidator;
import com.neathorium.core.extensions.namespaces.CoreUtilities;

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

        final var status = CoreUtilities.isNonException(exception);
        return DataFactoryFunctions.getWith(result, status, status ? CoreFormatterConstants.INVOCATION_SUCCESSFUL : CoreFormatterConstants.INVOCATION_EXCEPTION, exception);
    }
}

package com.github.karsaii.core.extensions.namespaces.validators;

import com.github.karsaii.core.constants.validators.CoreFormatterConstants;
import com.github.karsaii.core.extensions.namespaces.FileUtilities;


import static com.github.karsaii.core.namespaces.validators.CoreFormatter.getNamedErrorMessageOrEmpty;
import static com.github.karsaii.core.namespaces.validators.CoreFormatter.isBlankMessageWithName;
import static org.apache.commons.lang3.StringUtils.isBlank;

public interface FileUtilitiesValidators {
    static String isExistingMessage(String path) {
        var message = isBlankMessageWithName(path, "Path");
        if (isBlank(message)) {
            if (!FileUtilities.isExisting(path)) {
                message += "File with path (\"" + path + "\") doesn't exist" + CoreFormatterConstants.END_LINE;
            }
        }

        return getNamedErrorMessageOrEmpty("isExistingMessage: ", message);
    }
}

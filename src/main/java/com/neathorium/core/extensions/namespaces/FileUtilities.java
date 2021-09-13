package com.neathorium.core.extensions.namespaces;

import com.neathorium.core.constants.CoreConstants;
import com.neathorium.core.constants.FileUtilitiesConstants;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

public interface FileUtilities {
    static boolean isExisting(String path) {
        var localPath = FileUtilitiesConstants.NULL_PATH;
        var exception = CoreConstants.EXCEPTION;
        try {
            localPath = Paths.get(path);
        } catch (InvalidPathException ex) {
            exception = ex;
        }

        return CoreUtilities.isNonException(exception) && Files.exists(localPath);
    }
}

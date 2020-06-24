package com.github.karsaii.core.extensions.namespaces;

import com.github.karsaii.core.constants.CoreConstants;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

public interface FileUtilities {
    static boolean isExisting(String path) {
        Path localPath = null;
        var exception = CoreConstants.EXCEPTION;
        try {
            localPath = Paths.get(path);
        } catch (InvalidPathException ex) {
            exception = ex;
        }

        return (
            CoreUtilities.isNonException(exception) &&
            NullableFunctions.isNotNull(localPath) &&
            Files.exists(localPath)
        );
    }
}

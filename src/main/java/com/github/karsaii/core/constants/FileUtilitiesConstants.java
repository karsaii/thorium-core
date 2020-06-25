package com.github.karsaii.core.constants;

import com.github.karsaii.core.constants.validators.CoreFormatterConstants;

import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class FileUtilitiesConstants {
    public static final Path NULL_PATH = Paths.get(CoreFormatterConstants.EMPTY);
}

package com.neathorium.core.constants;

import com.neathorium.core.constants.validators.CoreFormatterConstants;

import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class FileUtilitiesConstants {
    public static final Path NULL_PATH = Paths.get(CoreFormatterConstants.EMPTY);
}

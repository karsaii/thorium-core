package com.github.karsaii.core.constants.process;

import com.github.karsaii.core.constants.systemidentity.BasicSystemIdentityConstants;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Map.entry;

public abstract class ProcessConstants {
    public static final String WIN_NULL_FILE_PATH = "NUL";
    public static final String ANY_NULL_FILE_PATH = "/dev/null";

    public static final ProcessBuilder NULL_BUILDER = new ProcessBuilder();

    public static final Map<String, String> NULL_FILE_PLATFORM_MAP = (
        Collections.unmodifiableMap(
            new LinkedHashMap<>(
                Map.ofEntries(
                    entry(BasicSystemIdentityConstants.WINDOWS, WIN_NULL_FILE_PATH),
                    entry(BasicSystemIdentityConstants.LINUX, ANY_NULL_FILE_PATH),
                    entry(BasicSystemIdentityConstants.MAC, ANY_NULL_FILE_PATH),
                    entry(BasicSystemIdentityConstants.UNKNOWN, ANY_NULL_FILE_PATH)
                )
            )
        )
    );
}

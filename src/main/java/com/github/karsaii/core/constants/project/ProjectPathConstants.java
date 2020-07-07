package com.github.karsaii.core.constants.project;

public abstract class ProjectPathConstants {
    public static final String PLATFORM_INDEPENDENT_SEPARATOR = "/";
    public static final String PROJECT_ROOT = System.getProperty("user.dir").replaceAll("\\\\", PLATFORM_INDEPENDENT_SEPARATOR);
}

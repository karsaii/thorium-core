package com.github.karsaii.core.constants;

import com.github.karsaii.core.extensions.namespaces.NullableFunctions;
import com.github.karsaii.core.records.Data;

import java.util.function.Predicate;

public abstract class ExecutionStateDataConstants {
    public static final Predicate<Data<?>> DEFAULT_FILTER = NullableFunctions::isNotNull;
}

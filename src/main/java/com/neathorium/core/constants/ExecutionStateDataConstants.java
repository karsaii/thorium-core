package com.neathorium.core.constants;

import com.neathorium.core.extensions.namespaces.NullableFunctions;
import com.neathorium.core.records.Data;

import java.util.function.Predicate;

public abstract class ExecutionStateDataConstants {
    public static final Predicate<Data<?>> DEFAULT_FILTER = NullableFunctions::isNotNull;
}

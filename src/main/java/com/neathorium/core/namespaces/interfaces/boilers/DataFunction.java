package com.neathorium.core.namespaces.interfaces.boilers;

import com.neathorium.core.records.Data;

import java.util.function.Function;

public interface DataFunction<T, U> extends Function<T, Data<U>> {
}

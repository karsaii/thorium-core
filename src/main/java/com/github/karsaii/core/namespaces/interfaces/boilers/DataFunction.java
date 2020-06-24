package com.github.karsaii.core.namespaces.interfaces.boilers;

import com.github.karsaii.core.records.Data;

import java.util.function.Function;

public interface DataFunction<T, U> extends Function<T, Data<U>> {
}

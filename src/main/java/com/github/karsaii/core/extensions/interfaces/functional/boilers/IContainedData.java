package com.github.karsaii.core.extensions.interfaces.functional.boilers;

import com.github.karsaii.core.records.Data;

import java.util.function.Function;

@FunctionalInterface
public interface IContainedData<T, U> extends IContained<T, Data<U>> {
    Function<T, Data<U>> get();
}

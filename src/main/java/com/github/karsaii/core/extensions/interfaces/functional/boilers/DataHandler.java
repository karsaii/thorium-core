package com.github.karsaii.core.extensions.interfaces.functional.boilers;

import com.github.karsaii.core.records.Data;

import java.util.function.Function;

@FunctionalInterface
public interface DataHandler<DependencyType, ReturnType> extends Function<Data<DependencyType>, Data<ReturnType>> {
    default Function<Data<DependencyType>, Data<ReturnType>> getIdentity() {
        return this;
    }
}

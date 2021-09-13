package com.neathorium.core.extensions.interfaces.functional.boilers;

import com.neathorium.core.records.Data;

import java.util.function.Function;

@FunctionalInterface
public interface DataHandler<DependencyType, ReturnType> extends Function<Data<DependencyType>, Data<ReturnType>> {
    default Function<Data<DependencyType>, Data<ReturnType>> getIdentity() {
        return this;
    }
}

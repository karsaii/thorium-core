package com.github.karsaii.core.namespaces.factories;

import com.github.karsaii.core.extensions.interfaces.functional.boilers.DataSupplier;
import com.github.karsaii.core.records.Data;

import java.util.function.Function;

public interface DataSupplierFactory {
    static <T> DataSupplier<T> get(Function<Void, Data<T>> function) {
        return function::apply;
    }
}

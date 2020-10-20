package com.github.karsaii.core.extensions.interfaces.functional.boilers;

import com.github.karsaii.core.records.Data;

import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
public interface DataSupplier<T> extends Function<Void, Data<T>> {
    default Data<T> apply() {
        return apply(null);
    }

    default Data<T> get() {
        return apply(null);
    }

    default Function<Void, Data<T>> getInterfaceIdentity() {
        return this;
    }

    default Supplier<Data<T>> getSupplier() {
        return this::apply;
    }

    default Supplier<Data<?>> getSupplierRaw() {
        return this::apply;
    }
}

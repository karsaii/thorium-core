package com.github.karsaii.core.extensions.namespaces.asserts;

import com.github.karsaii.core.extensions.interfaces.functional.TriConsumer;
import com.github.karsaii.core.namespaces.DataFunctions;
import com.github.karsaii.core.records.Data;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface JUnitAdapter {
    static <Actual, Expected> Consumer<Data<Actual>> doAssert(TriConsumer<String, Expected, Actual> assertion, Expected expected, String message) {
        return data -> assertion.accept(message, expected, DataFunctions.getObject(data));
    }

    static <Actual, Expected> Consumer<Data<Actual>> doAssert(TriConsumer<String, Expected, Actual> assertion, Expected expected) {
        return data -> assertion.accept(DataFunctions.getFormattedMessage(data), expected, DataFunctions.getObject(data));
    }

    static <Actual> Consumer<Data<Actual>> doAssert(BiConsumer<String, Actual> assertion, String message) {
        return data -> assertion.accept(message, DataFunctions.getObject(data));
    }

    static <Actual> Consumer<Data<Actual>> doAssert(BiConsumer<String, Actual> assertion) {
        return data -> assertion.accept(DataFunctions.getFormattedMessage(data), data.object);
    }

    static <Actual, Expected> Consumer<Data<Actual>> doAssertSupplier(TriConsumer<Supplier<String>, Expected, Actual> assertion, Expected expected, Supplier<String> message) {
        return data -> assertion.accept(message, expected, DataFunctions.getObject(data));
    }

    static <Actual, Expected> Consumer<Data<Actual>> doAssertSupplier(TriConsumer<Supplier<String>, Expected, Actual> assertion, Expected expected) {
        return data -> assertion.accept(() -> DataFunctions.getFormattedMessage(data), expected, DataFunctions.getObject(data));
    }

    static <Actual> Consumer<Data<Actual>> doAssertSupplier(BiConsumer<Supplier<String>, Actual> assertion, Supplier<String> message) {
        return data -> assertion.accept(message, DataFunctions.getObject(data));
    }

    static <Actual> Consumer<Data<Actual>> doAssertSupplier(BiConsumer<Supplier<String>, Actual> assertion) {
        return data -> assertion.accept(() -> DataFunctions.getFormattedMessage(data), DataFunctions.getObject(data));
    }

    static <Actual, Expected> Consumer<Data<Actual>> doAssert(BiConsumer<Expected, Actual> assertion, Expected expected) {
        return data -> assertion.accept(expected, DataFunctions.getObject(data));
    }

    static <Actual> Consumer<Data<Actual>> doAssert(Consumer<Actual> assertion) {
        return data -> assertion.accept(DataFunctions.getObject(data));
    }
}

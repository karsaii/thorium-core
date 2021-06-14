package com.github.karsaii.core.extensions.namespaces.asserts;

import com.github.karsaii.core.extensions.interfaces.functional.TriConsumer;
import com.github.karsaii.core.namespaces.DataFunctions;
import com.github.karsaii.core.records.Data;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface TestNGAdapter {
    static <Actual, Expected> Consumer<Data<Actual>> doAssert(TriConsumer<Actual, Expected, String> assertion, Expected expected, String message) {
        return data -> assertion.accept(DataFunctions.getObject(data), expected, message);
    }

    static <Actual, Expected> Consumer<Data<Actual>> doAssert(TriConsumer<Actual, Expected, String> assertion, Expected expected) {
        return data -> assertion.accept(DataFunctions.getObject(data), expected, DataFunctions.getFormattedMessage(data));
    }

    static <Actual, Expected> Consumer<Data<Actual>> doAssert(BiConsumer<Actual, Expected> assertion, Expected expected) {
        return data -> assertion.accept(DataFunctions.getObject(data), expected);
    }

    static <Actual> Consumer<Data<Actual>> doAssert(BiConsumer<Actual, String> assertion, String message) {
        return data -> assertion.accept(DataFunctions.getObject(data), message);
    }

    static <Actual> Consumer<Data<Actual>> doAssert(BiConsumer<Actual, String> assertion) {
        return data -> assertion.accept(DataFunctions.getObject(data), DataFunctions.getFormattedMessage(data));
    }

    static <Actual> Consumer<Data<Actual>> doAssert(Consumer<Actual> assertion) {
        return data -> assertion.accept(DataFunctions.getObject(data));
    }
}

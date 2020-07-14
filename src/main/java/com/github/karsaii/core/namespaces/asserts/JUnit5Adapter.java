package com.github.karsaii.core.namespaces.asserts;

import com.github.karsaii.core.extensions.interfaces.functional.TriConsumer;
import com.github.karsaii.core.records.Data;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface JUnit5Adapter {
    static <Actual, Expected> Consumer<Data<Actual>> doAssert(TriConsumer<Expected, Actual, String> assertion, Expected expected, String message) {
        return data -> assertion.accept(expected, data.object, message);
    }

    static <Actual, Expected> Consumer<Data<Actual>> doAssert(TriConsumer<Expected, Actual, String> assertion, Expected expected) {
        return data -> assertion.accept(expected, data.object, data.message.toString());
    }

    static <Actual> Consumer<Data<Actual>> doAssert(BiConsumer<Actual, String> assertion, String message) {
        return data -> assertion.accept(data.object, message);
    }

    static <Actual> Consumer<Data<Actual>> doAssert(BiConsumer<Actual, String> assertion) {
        return data -> assertion.accept(data.object, data.message.toString());
    }

    static <Actual, Expected> Consumer<Data<Actual>> doAssertSupplier(TriConsumer<Expected, Actual, Supplier<String>> assertion, Expected expected, Supplier<String> message) {
        return data -> assertion.accept(expected, data.object, message);
    }

    static <Actual, Expected> Consumer<Data<Actual>> doAssertSupplier(TriConsumer<Expected, Actual, Supplier<String>> assertion, Expected expected) {
        return data -> assertion.accept(expected, data.object, data.message::toString);
    }

    static <Actual> Consumer<Data<Actual>> doAssertSupplier(BiConsumer<Actual, Supplier<String>> assertion, Supplier<String> message) {
        return data -> assertion.accept(data.object, message);
    }

    static <Actual> Consumer<Data<Actual>> doAssertSupplier(BiConsumer<Actual, Supplier<String>> assertion) {
        return data -> assertion.accept(data.object, data.message::toString);
    }

    static <Actual, Expected> Consumer<Data<Actual>> doAssert(BiConsumer<Expected, Actual> assertion, Expected expected) {
        return data -> assertion.accept(expected, data.object);
    }

    static <Actual> Consumer<Data<Actual>> doAssert(Consumer<Actual> assertion) {
        return data -> assertion.accept(data.object);
    }
}

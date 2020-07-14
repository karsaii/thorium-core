package com.github.karsaii.core.namespaces.asserts;

import com.github.karsaii.core.extensions.interfaces.functional.TriConsumer;
import com.github.karsaii.core.records.Data;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface JUnit5StatusAdapter {
    static <Actual> Consumer<Data<Actual>> doAssert(TriConsumer<Boolean, Boolean, String> assertion, Boolean expected, String message) {
        return data -> assertion.accept(expected, data.status, message);
    }

    static <Actual> Consumer<Data<Actual>> doAssert(TriConsumer<Boolean, Boolean, String> assertion, Boolean expected) {
        return data -> assertion.accept(expected, data.status, data.message.toString());
    }

    static <Actual> Consumer<Data<Actual>> doAssert(BiConsumer<Boolean, String> assertion, String message) {
        return data -> assertion.accept(data.status, message);
    }

    static <Actual> Consumer<Data<Actual>> doAssert(BiConsumer<Boolean, String> assertion) {
        return data -> assertion.accept(data.status, data.message.toString());
    }

    static <Actual> Consumer<Data<Actual>> doAssertSupplier(TriConsumer<Boolean, Boolean, Supplier<String>> assertion, Boolean expected, Supplier<String> message) {
        return data -> assertion.accept(expected, data.status, message);
    }

    static <Actual> Consumer<Data<Actual>> doAssertSupplier(TriConsumer<Boolean, Boolean, Supplier<String>> assertion, Boolean expected) {
        return data -> assertion.accept(expected, data.status, data.message::toString);
    }

    static <Actual> Consumer<Data<Actual>> doAssertSupplier(BiConsumer<Boolean, Supplier<String>> assertion, Supplier<String> message) {
        return data -> assertion.accept(data.status, message);
    }

    static <Actual> Consumer<Data<Actual>> doAssertSupplier(BiConsumer<Boolean, Supplier<String>> assertion) {
        return data -> assertion.accept(data.status, data.message::toString);
    }

    static <Actual, Expected> Consumer<Data<Actual>> doAssert(BiConsumer<Boolean, Boolean> assertion, Boolean expected) {
        return data -> assertion.accept(expected, data.status);
    }

    static <Actual> Consumer<Data<Actual>> doAssert(Consumer<Boolean> assertion) {
        return data -> assertion.accept(data.status);
    }
}

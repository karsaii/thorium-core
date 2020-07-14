package com.github.karsaii.core.namespaces.asserts;

import com.github.karsaii.core.extensions.interfaces.functional.TriConsumer;
import com.github.karsaii.core.records.Data;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface JUnit5StatusAdapter {
    static Consumer<Data<Object>> doAssert(TriConsumer<Boolean, Boolean, String> assertion, Boolean expected, String message) {
        return data -> assertion.accept(expected, data.status, message);
    }

    static Consumer<Data<Object>> doAssert(TriConsumer<Boolean, Boolean, String> assertion, Boolean expected) {
        return data -> assertion.accept(expected, data.status, data.message.toString());
    }

    static Consumer<Data<Object>> doAssert(BiConsumer<Boolean, String> assertion, String message) {
        return data -> assertion.accept(data.status, message);
    }

    static Consumer<Data<Object>> doAssert(BiConsumer<Boolean, String> assertion) {
        return data -> assertion.accept(data.status, data.message.toString());
    }

    static Consumer<Data<Object>> doAssertSupplier(TriConsumer<Boolean, Boolean, Supplier<String>> assertion, Boolean expected, Supplier<String> message) {
        return data -> assertion.accept(expected, data.status, message);
    }

    static Consumer<Data<Object>> doAssertSupplier(TriConsumer<Boolean, Boolean, Supplier<String>> assertion, Boolean expected) {
        return data -> assertion.accept(expected, data.status, data.message::toString);
    }

    static Consumer<Data<Object>> doAssertSupplier(BiConsumer<Boolean, Supplier<String>> assertion, Supplier<String> message) {
        return data -> assertion.accept(data.status, message);
    }

    static Consumer<Data<Object>> doAssertSupplier(BiConsumer<Boolean, Supplier<String>> assertion) {
        return data -> assertion.accept(data.status, data.message::toString);
    }

    static Consumer<Data<Object>> doAssert(BiConsumer<Boolean, Boolean> assertion, Boolean expected) {
        return data -> assertion.accept(expected, data.status);
    }

    static Consumer<Data<Object>> doAssert(Consumer<Boolean> assertion) {
        return data -> assertion.accept(data.status);
    }
}

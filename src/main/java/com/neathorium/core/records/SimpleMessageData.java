package com.neathorium.core.records;

import com.neathorium.core.extensions.interfaces.functional.boilers.IGetMessage;
import com.neathorium.core.namespaces.validators.CoreFormatter;
import com.neathorium.core.extensions.namespaces.NullableFunctions;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class SimpleMessageData implements IGetMessage {
    public final BiFunction<String, Boolean, String> formatter;
    public final String message;

    public SimpleMessageData(BiFunction<String, Boolean, String> formatter, String message) {
        this.formatter = formatter;
        this.message = message;
    }

    public SimpleMessageData(String message) {
        this(null, message);
    }

    public SimpleMessageData() {
        this(null, null);
    }

    @Override
    public Function<Boolean, String> get() {
        final var isFormatterNull = NullableFunctions.isNull(formatter);
        final var isMessageBlank = isBlank(message);
        if (isFormatterNull && isMessageBlank) {
            return CoreFormatter.isFormatterNullAndMessageBlank();
        }

        if (isFormatterNull) {
            return CoreFormatter.isFormatterNull(message);
        }

        if (isMessageBlank) {
            return CoreFormatter.isMessageBlank(formatter);
        }

        return CoreFormatter.isFormatterAndMessageValid(formatter, message);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final var that = (SimpleMessageData) o;
        return Objects.equals(formatter, that.formatter) && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(formatter, message);
    }
}

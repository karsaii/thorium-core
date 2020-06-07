package com.github.karsaii.core.reflection.message;

import com.github.karsaii.core.records.reflection.message.InvokeCommonMessageParametersData;

import java.util.Objects;
import java.util.function.Function;

public class RegularMessageData implements Function<InvokeCommonMessageParametersData, Function<Exception, String>> {
    public final Function<InvokeCommonMessageParametersData, Function<Exception, String>> constructor;

    public RegularMessageData(Function<InvokeCommonMessageParametersData, Function<Exception, String>> constructor) {
        this.constructor = constructor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final var that = (RegularMessageData) o;
        return Objects.equals(constructor, that.constructor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(constructor);
    }

    @Override
    public Function<Exception, String> apply(InvokeCommonMessageParametersData data) {
        return ex -> constructor.apply(new InvokeCommonMessageParametersData(data.message, data.returnType, data.parameterTypes)).apply(ex);
    }
}

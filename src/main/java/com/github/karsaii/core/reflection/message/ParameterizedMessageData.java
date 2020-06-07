package com.github.karsaii.core.reflection.message;

import com.github.karsaii.core.records.reflection.message.InvokeCommonMessageParametersData;
import com.github.karsaii.core.records.reflection.message.InvokeParameterizedMessageData;

import java.util.Objects;
import java.util.function.Function;

public class ParameterizedMessageData implements Function<InvokeCommonMessageParametersData, Function<Exception, String>> {
    public final String parameter;
    public final Function<InvokeParameterizedMessageData, Function<Exception, String>> constructor;

    public ParameterizedMessageData(String parameter, Function<InvokeParameterizedMessageData, Function<Exception, String>> constructor) {
        this.parameter = parameter;
        this.constructor = constructor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final var that = (ParameterizedMessageData) o;
        return Objects.equals(parameter, that.parameter) && Objects.equals(constructor, that.constructor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parameter, constructor);
    }

    @Override
    public Function<Exception, String> apply(InvokeCommonMessageParametersData data) {
        return ex -> constructor.apply(new InvokeParameterizedMessageData(data.message, data.returnType, data.parameterTypes, parameter)).apply(ex);
    }
}

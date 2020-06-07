package com.github.karsaii.core.records;

import java.util.Objects;
import java.util.function.Function;

public class HandleResultData<ParameterType, ReturnType> {
    public final Function<ParameterType, ReturnType> caster;
    public final ParameterType parameter;
    public final ReturnType defaultValue;

    public HandleResultData(Function<ParameterType, ReturnType> caster, ParameterType parameter, ReturnType defaultValue) {
        this.caster = caster;
        this.parameter = parameter;
        this.defaultValue = defaultValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final var that = (HandleResultData<?, ?>) o;
        return Objects.equals(caster, that.caster) && Objects.equals(parameter, that.parameter) && Objects.equals(defaultValue, that.defaultValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(caster, parameter, defaultValue);
    }
}

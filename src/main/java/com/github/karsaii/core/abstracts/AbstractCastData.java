package com.github.karsaii.core.abstracts;

import java.util.Objects;
import java.util.function.Function;

public abstract class AbstractCastData<T, U> {
    public final T defaultValue;
    public final Function<Object, U> caster;

    public AbstractCastData(T defaultValue, Function<Object, U> caster) {
        this.defaultValue = defaultValue;
        this.caster = caster;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var that = (AbstractCastData<?, ?>) o;
        return Objects.equals(defaultValue, that.defaultValue) && Objects.equals(caster, that.caster);
    }

    @Override
    public int hashCode() {
        return Objects.hash(defaultValue, caster);
    }
}

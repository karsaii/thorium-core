package com.github.karsaii.core.records.caster;

import com.github.karsaii.core.abstracts.AbstractCastData;
import com.github.karsaii.core.records.Data;

import java.util.function.Function;

public class WrappedCastData<T> extends AbstractCastData<Data<T>, T> {
    public WrappedCastData(Data<T> object, Function<Object, T> caster) {
        super(object, caster);
    }
}

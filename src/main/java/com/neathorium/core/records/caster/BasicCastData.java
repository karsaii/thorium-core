package com.neathorium.core.records.caster;

import com.neathorium.core.abstracts.AbstractCastData;

import java.util.function.Function;

public class BasicCastData<T> extends AbstractCastData<T, T> {
    public BasicCastData(T object, Function<Object, T> caster) {
        super(object, caster);
    }
}

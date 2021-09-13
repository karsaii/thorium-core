package com.neathorium.core.records.caster;

import com.neathorium.core.abstracts.AbstractCastData;
import com.neathorium.core.records.Data;

import java.util.function.Function;

public class WrappedCastData<T> extends AbstractCastData<Data<T>, T> {
    public WrappedCastData(Data<T> object, Function<Object, T> caster) {
        super(object, caster);
    }
}

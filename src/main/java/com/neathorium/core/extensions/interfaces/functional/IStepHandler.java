package com.neathorium.core.extensions.interfaces.functional;

import com.neathorium.core.records.Data;

@FunctionalInterface
public interface IStepHandler {
    <Any> Data<Any> apply(int index);
}

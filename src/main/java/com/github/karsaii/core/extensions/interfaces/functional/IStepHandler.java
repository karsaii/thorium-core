package com.github.karsaii.core.extensions.interfaces.functional;

import com.github.karsaii.core.records.Data;

@FunctionalInterface
public interface IStepHandler {
    <Any> Data<Any> apply(int index);
}

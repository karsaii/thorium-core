package com.neathorium.core.extensions.interfaces.functional.boilers;

import java.util.function.Function;

@FunctionalInterface
public interface IContained<T, U> {
    Function<T, U> get();
}

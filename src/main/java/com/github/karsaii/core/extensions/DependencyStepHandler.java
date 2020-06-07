package com.github.karsaii.core.extensions;

import com.github.karsaii.core.extensions.interfaces.functional.IStepHandler;
import com.github.karsaii.core.records.Data;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

public class DependencyStepHandler<DependencyType, ReturnType> implements IStepHandler {
    public final Function<DependencyType, Data<?>>[] steps;
    public final DependencyType dependency;

    public DependencyStepHandler(Function<DependencyType, Data<?>>[] steps, DependencyType dependency) {
        this.steps = steps;
        this.dependency = dependency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var that = (DependencyStepHandler<?, ?>) o;
        return Arrays.equals(steps, that.steps) && Objects.equals(dependency, that.dependency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(steps, dependency);
    }

    @Override
    public <Any> Data<Any> apply(int index) {
        return (Data<Any>) steps[index].apply(dependency);
    }
}

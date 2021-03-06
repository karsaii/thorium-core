package com.github.karsaii.core.namespaces;

import com.github.karsaii.core.records.Data;

import java.util.function.Function;
import java.util.function.Predicate;

import static com.github.karsaii.core.extensions.namespaces.CoreUtilities.areNotNull;
import static com.github.karsaii.core.extensions.namespaces.NullableFunctions.isNotNull;
import static com.github.karsaii.core.namespaces.DataFactoryFunctions.replaceMessage;
import static org.apache.commons.lang3.StringUtils.isBlank;

public interface BaseExecutionFunctions {
    static <DependencyType, ParameterType, ReturnType> Function<DependencyType, ReturnType> conditionalChain(Predicate<ParameterType> guard, Function<DependencyType, ParameterType> dependency, Function<ParameterType, ReturnType> positive, ReturnType negative) {
        return d -> {
            final var positiveDependency = dependency.apply(d);
            return guard.test(positiveDependency) ? positive.apply(positiveDependency) : negative;
        };
    }

    static <DependencyType, ParameterType, ReturnType> Function<DependencyType, ReturnType> conditionalChain(Function<ParameterType, String> guard, Function<DependencyType, ParameterType> dependency, Function<ParameterType, ReturnType> positive, ReturnType negative) {
        return d -> {
            final var positiveDependency = dependency.apply(d);
            final var guardMessage = guard.apply(positiveDependency);
            return isBlank(guardMessage) ? positive.apply(positiveDependency) : negative;
        };
    }

    static <ReturnType> ReturnType ifDependencyAnyCore(ReturnType data) {
        return isNotNull(data) ? data : null;
    }

    private static <DependencyType, ReturnType> ReturnType ifDependencyAnyWrappedCore(DependencyType dependency, Function<DependencyType, ReturnType> function) {
        return ifDependencyAnyCore(function.apply(dependency));
    }

    private static <DependencyType, ReturnType> Function<DependencyType, ReturnType> ifDependencyAnyWrappedCore(Function<DependencyType, ReturnType> function) {
        return dependency -> ifDependencyAnyWrappedCore(dependency, function);
    }

    static <DependencyType, ReturnType> Function<DependencyType, ReturnType> ifDependency(boolean condition, Function<DependencyType, ReturnType> positive, ReturnType negative) {
        return condition && isNotNull(positive) ? ifDependencyAnyWrappedCore(positive) : dependency -> ifDependencyAnyCore(negative);
    }

    static <DependencyType, ReturnType> Function<DependencyType, Data<ReturnType>> ifDependency(String nameof, String errorMessage, Function<DependencyType, Data<ReturnType>> positive, Data<ReturnType> negative) {
        return ifDependency(isBlank(errorMessage), positive, replaceMessage(negative, nameof, errorMessage));
    }

    static <DependencyType, ParameterType, ReturnType> Function<DependencyType, ReturnType> ifDependency(
        boolean status,
        Predicate<ParameterType> guard,
        Function<DependencyType, ParameterType> function,
        Function<ParameterType, ReturnType> positive,
        ReturnType negative
    ) {
        return ifDependency(status && areNotNull(guard, function, positive), BaseExecutionFunctions.conditionalChain(guard, function, positive, negative), negative);
    }

    static <DependencyType, ParameterType, ReturnType> Function<DependencyType, ReturnType> ifDependency(
        boolean status,
        Function<ParameterType, String> guard,
        Function<DependencyType, ParameterType> function,
        Function<ParameterType, ReturnType> positive,
        ReturnType negative
    ) {
        return ifDependency(status && areNotNull(guard, function, positive), BaseExecutionFunctions.conditionalChain(guard, function, positive, negative), negative);
    }
}

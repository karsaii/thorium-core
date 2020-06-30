package com.github.karsaii.core.namespaces;

import com.github.karsaii.core.constants.validators.CoreFormatterConstants;
import com.github.karsaii.core.namespaces.validators.CoreFormatter;
import com.github.karsaii.core.records.Data;

import java.util.function.Function;
import java.util.function.Predicate;

import static com.github.karsaii.core.extensions.namespaces.CoreUtilities.areNotNull;
import static com.github.karsaii.core.extensions.namespaces.NullableFunctions.isNotNull;
import static com.github.karsaii.core.namespaces.DataFactoryFunctions.prependMessage;
import static com.github.karsaii.core.namespaces.DataFactoryFunctions.replaceMessage;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public interface DataExecutionFunctions {
    static <DependencyType, ParameterType, ReturnType> Function<DependencyType, Data<ReturnType>> conditionalChain(
        Predicate<Data<ParameterType>> guard,
        Function<DependencyType, Data<ParameterType>> dependency,
        Function<Data<ParameterType>, Data<ReturnType>> positive,
        Data<ReturnType> negative
    ) {
        return BaseExecutionFunctions.conditionalChain(guard, dependency, positive, prependMessage(negative, "conditionalChain", "Dependency parameter failed the guard" + CoreFormatterConstants.COLON_SPACE + "guard message was false" + CoreFormatterConstants.END_LINE));
    }

    static <DependencyType, ParameterType, ReturnType> Function<DependencyType, Data<ReturnType>> conditionalChain(
        Function<Data<ParameterType>, String> guard,
        Function<DependencyType, Data<ParameterType>> dependency,
        Function<Data<ParameterType>, Data<ReturnType>> positive,
        Data<ReturnType> negative
    ) {
        return d -> {
            final var positiveDependency = dependency.apply(d);
            final var guardMessage = guard.apply(positiveDependency);
            return isBlank(guardMessage) ? positive.apply(positiveDependency) : prependMessage(negative, "conditionalChain", "Dependency parameter failed the guard" + CoreFormatterConstants.COLON_SPACE + guardMessage);
        };
    }

    static <DependencyType, ParameterType, ReturnType> Function<DependencyType, Data<ReturnType>> conditionalUnwrapChain(
        Predicate<Data<ParameterType>> guard,
        Function<DependencyType, Data<ParameterType>> dependency,
        Function<ParameterType, Data<ReturnType>> positive,
        Data<ReturnType> negative
    ) {
        return d -> {
            final var positiveDependency = dependency.apply(d);
            return guard.test(positiveDependency) ? positive.apply(positiveDependency.object) : negative;
        };
    }

    static <DependencyType, ParameterType, ReturnType> Function<DependencyType, Data<ReturnType>> conditionalUnwrapChain(
        Function<Data<ParameterType>, String> guard,
        Function<DependencyType, Data<ParameterType>> dependency,
        Function<ParameterType, Data<ReturnType>> positive,
        Data<ReturnType> negative
    ) {
        return d -> {
            final var positiveDependency = dependency.apply(d);
            final var guardMessage = guard.apply(positiveDependency);
            return isBlank(guardMessage) ? positive.apply(positiveDependency.object) : prependMessage(negative, "conditionalChain", "Dependency parameter failed the guard" + CoreFormatterConstants.COLON_SPACE + guardMessage);
        };
    }

    static <DependencyType, ParameterType, ReturnType> Function<DependencyType, Data<ReturnType>> validChain(Function<DependencyType, Data<ParameterType>> dependency, Function<Data<ParameterType>, Data<ReturnType>> positive, Data<ReturnType> negative) {
        return conditionalChain(CoreFormatter::isInvalidOrFalseMessage, dependency, positive, negative);
    }

    static <ReturnType> Data<ReturnType> ifDependencyAnyCore(String nameof, Data<ReturnType> data) {
        final var name = isNotBlank(nameof) ? nameof : "ifDependencyAnyCore";
        return isNotNull(data) ? (
            DataFactoryFunctions.getWithNameAndMessage(data.object, data.status, DataFunctions.getNameIfAbsent(data, name), data.message.message, data.exception)
        ) : DataFactoryFunctions.getInvalidWithNameAndMessage(null, name, "Data " + CoreFormatterConstants.WAS_NULL);
    }

    private static <DependencyType, ReturnType> Data<ReturnType> ifDependencyAnyWrappedCore(DependencyType dependency, String nameof, Function<DependencyType, Data<ReturnType>> function) {
        return ifDependencyAnyCore(nameof, function.apply(dependency));
    }

    private static <DependencyType, ReturnType> Function<DependencyType, Data<ReturnType>> ifDependencyAnyWrappedCore(String nameof, Function<DependencyType, Data<ReturnType>> function) {
        return dependency -> ifDependencyAnyWrappedCore(dependency, nameof, function);
    }

    static <DependencyType, ReturnType> Function<DependencyType, Data<ReturnType>> ifDependency(String nameof, boolean condition, Function<DependencyType, Data<ReturnType>> positive, Data<ReturnType> negative) {
        final var lNameof = isBlank(nameof) ? "ifDependency" : nameof;
        return condition && isNotNull(positive) ? ifDependencyAnyWrappedCore(lNameof, positive) : dependency -> ifDependencyAnyCore(lNameof, negative);
    }

    static <DependencyType, ReturnType> Function<DependencyType, Data<ReturnType>> ifDependency(String nameof, String errorMessage, Function<DependencyType, Data<ReturnType>> positive, Data<ReturnType> negative) {
        return ifDependency(nameof, isBlank(errorMessage), positive, replaceMessage(negative, nameof, errorMessage));
    }

    static <DependencyType, ParameterType, ReturnType> Function<DependencyType, Data<ReturnType>> ifDependency(
        String nameof,
        boolean status,
        Predicate<Data<ParameterType>> guard,
        Function<DependencyType, Data<ParameterType>> function,
        Function<Data<ParameterType>, Data<ReturnType>> positive,
        Data<ReturnType> negative
    ) {
        return ifDependency(nameof, status && areNotNull(guard, function, positive), conditionalChain(guard, function, positive, negative), negative);
    }

    static <DependencyType, ParameterType, ReturnType> Function<DependencyType, Data<ReturnType>> ifDependency(
        String nameof,
        boolean status,
        Function<Data<ParameterType>, String> guard,
        Function<DependencyType, Data<ParameterType>> function,
        Function<Data<ParameterType>, Data<ReturnType>> positive,
        Data<ReturnType> negative
    ) {
        return ifDependency(nameof, status && areNotNull(guard, function, positive), conditionalChain(guard, function, positive, negative), negative);
    }
}

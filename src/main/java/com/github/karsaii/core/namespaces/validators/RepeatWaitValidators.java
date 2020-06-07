package com.github.karsaii.core.namespaces.validators;

import com.github.karsaii.core.records.Data;
import com.github.karsaii.core.records.WaitTimeData;

import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.apache.commons.lang3.StringUtils.isBlank;

public interface RepeatWaitValidators {
    static <T, V> String validateUntilParameters(Function<T, Data<?>>[] functions, BiPredicate<Predicate<Data<?>>, Data<?>[]> continueCondition, WaitTimeData timeData) {
        final var message = (
            CoreFormatter.isNullMessageWithName(functions, "Functions") +
            CoreFormatter.isNullMessageWithName(continueCondition, "ContinueCondition") +
            WaitValidators.validateWaitTimeData(timeData)
        );

        return isBlank(message) ? message : ("Wait.until: " + message);
    }
}

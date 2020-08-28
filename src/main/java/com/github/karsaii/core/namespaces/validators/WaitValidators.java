package com.github.karsaii.core.namespaces.validators;

import com.github.karsaii.core.records.wait.WaitTimeData;
import com.github.karsaii.core.constants.validators.CoreFormatterConstants;

import java.util.function.Function;
import java.util.function.Predicate;

import static org.apache.commons.lang3.StringUtils.isBlank;

public interface WaitValidators {
    static String validateWaitTimeData(WaitTimeData timeData) {
        var message = CoreFormatter.isNullMessageWithName(timeData, "TimeData");
        if (isBlank(message)) {
            message += (
                CoreFormatter.isNullMessageWithName(timeData.clock, "TimeData clock") +
                CoreFormatter.isNullMessageWithName(timeData.interval, "TimeData interval") +
                CoreFormatter.isNullMessageWithName(timeData.duration, "TimeData duration")
            );
        }

        return isBlank(message) ? message : (CoreFormatterConstants.PARAMETER_ISSUES + message);
    }

    static <T, V> String validateUntilParameters(Function<T, V> condition, Predicate<V> continueCondition, WaitTimeData timeData) {
        final var message = (
            CoreFormatter.isNullMessageWithName(condition, "Condition") +
            CoreFormatter.isNullMessageWithName(continueCondition, "ContinueCondition") +
            validateWaitTimeData(timeData)
        );

        return isBlank(message) ? message : ("validateUntilParameters: " + message);
    }

    static <T, V> String validateUntilParametersRepeat(Function<T, ?> condition, Predicate<?> continueCondition, WaitTimeData timeData) {
        final var message = (
            CoreFormatter.isNullMessageWithName(condition, "Condition") +
            CoreFormatter.isNullMessageWithName(continueCondition, "ContinueCondition") +
            validateWaitTimeData(timeData)
        );

        return isBlank(message) ? message : ("validateUntilParametersRepeat: " + message);
    }
}

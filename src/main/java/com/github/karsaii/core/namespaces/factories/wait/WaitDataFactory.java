package com.github.karsaii.core.namespaces.factories.wait;

import com.github.karsaii.core.constants.validators.CoreFormatterConstants;
import com.github.karsaii.core.records.wait.WaitData;
import com.github.karsaii.core.records.wait.tasks.common.WaitTaskCommonData;
import com.github.karsaii.core.records.wait.WaitTimeData;

import java.util.function.Function;
import java.util.function.Predicate;

public interface WaitDataFactory {
    static <T, U, V> WaitData<T, U, V> getWith(Function<T, U> function, Predicate<V> exitCondition, String conditionMessage, WaitTimeData timeData) {
        return new WaitData<>(new WaitTaskCommonData<>(function, exitCondition), conditionMessage, timeData);
    }

    static <T, U, V> WaitData<T, U, V> getWithIntervalAndTimeout(Function<T, U> function, Predicate<V> exitCondition, String conditionMessage, int interval, int timeout) {
        return getWith(function, exitCondition, conditionMessage, WaitTimeDataFactory.getWithDefaultClock(interval, timeout));
    }

    static <T, U, V> WaitData<T, U, V> getWithSleepDuration(Function<T, U> function, Predicate<V> exitCondition, int duration) {
        return getWithIntervalAndTimeout(function, exitCondition, "Sleeping for " + duration + " milliseconds" + CoreFormatterConstants.END_LINE, 0, duration);
    }

    static <T, U, V> WaitData<T, U, V> getWithDefaultTimeData(Function<T, U> function, Predicate<V> exitCondition, String conditionMessage) {
        return getWith(function, exitCondition, conditionMessage, WaitTimeDataFactory.getDefault());
    }

}

package com.github.karsaii.core.records.wait;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public class WaitData<T, U, V> {
    public final Function<T, U> function;
    public final Predicate<V> exitCondition;
    public final String conditionMessage;
    public final WaitTimeData timeData;

    public WaitData(Function<T, U> function, Predicate<V> exitCondition, String conditionMessage, WaitTimeData timeData) {
        this.function = function;
        this.exitCondition = exitCondition;
        this.conditionMessage = conditionMessage;
        this.timeData = timeData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final var waitData = (WaitData<?, ?, ?>) o;
        return (
            Objects.equals(function, waitData.function) &&
            Objects.equals(exitCondition, waitData.exitCondition) &&
            Objects.equals(conditionMessage, waitData.conditionMessage) &&
            Objects.equals(timeData, waitData.timeData)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(function, exitCondition, conditionMessage, timeData);
    }
}

package com.github.karsaii.core.extensions.namespaces;

import com.github.karsaii.core.constants.CardinalityDefaults;
import com.github.karsaii.core.extensions.interfaces.functional.QuadPredicate;
import com.github.karsaii.core.namespaces.validators.DataValidators;
import com.github.karsaii.core.records.CardinalityData;
import com.github.karsaii.core.records.Data;
import com.github.karsaii.core.records.executor.ExecutionStateData;
import org.apache.commons.lang3.StringUtils;
import com.github.karsaii.core.constants.CoreConstants;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public interface CoreUtilities {
    static boolean isEqual(Object a, Object b) {
        return Objects.equals(a, b);
    }

    static boolean isNotEqual(Object a, Object b) {
        return !isEqual(a, b);
    }

    static boolean isException(Exception ex) {
        final var exception = CoreConstants.EXCEPTION;
        return NullableFunctions.isNotNull(ex) && isNotEqual(exception, ex) && isNotEqual(exception.getMessage(), ex.getMessage());
    }

    static boolean isNonException(Exception ex) {
        final var exception = CoreConstants.EXCEPTION;
        return NullableFunctions.isNotNull(ex) && (isEqual(exception, ex) || isEqual(exception.getMessage(), ex.getMessage()));
    }

    static <T> boolean isBoolean(T object) {
        return NullableFunctions.isNotNull(object) && Objects.equals(Boolean.class.getTypeName(), object.getClass().getTypeName());
    }

    static <T> boolean isNotBoolean(T object) {
        return !isBoolean(object);
    }

    static <T> boolean isConditionCore(T object, Boolean value) {
        return isBoolean(object) && NullableFunctions.isNotNull(value) && Objects.equals(value, object);
    }

    static <T> boolean isTrue(T object) {
        return isConditionCore(object, Boolean.TRUE);
    }

    static <T> boolean isFalse(T object) {
        return isConditionCore(object, Boolean.FALSE);
    }

    @SafeVarargs
    static <T> boolean are(Predicate<T> condition, CardinalityData conditionData, T... objects) {
        if (NullableFunctions.isNull(condition) || ArrayFunctions.isNullOrEmptyArray(objects)) {
            return false;
        }

        final var guardValue = conditionData.guardValue;
        final var finalValue = conditionData.finalValue;
        final Function<Predicate<T>, Predicate<T>> inverter = CardinalitiesFunctions.getPredicate(conditionData.invert);
        final var checker = inverter.apply(condition);
        final var length = objects.length;
        var index = 0;
        if (length == 1) {
            return checker.test(objects[index]) ? guardValue : finalValue;
        }

        for(; index < length; ++index) {
            if (checker.test(objects[index])) {
                return guardValue;
            }
        }

        return finalValue;
    }

    @SafeVarargs
    static <T> boolean areAll(Predicate<T> condition, T... objects) {
        return are(condition, CardinalityDefaults.all, objects);
    }

    @SafeVarargs
    static <T> boolean areAny(Predicate<T> condition, T... objects) {
        return are(condition, CardinalityDefaults.any, objects);
    }

    @SafeVarargs
    static <T> boolean areNone(Predicate<T> condition, T... objects) {
        return are(condition, CardinalityDefaults.none, objects);
    }

    @SafeVarargs
    static <T> boolean areNotNull(T... objects) {
        return areAll(NullableFunctions::isNotNull, objects);
    }

    @SafeVarargs
    static <T> boolean areNull(T... objects) {
        return areAll(NullableFunctions::isNull, objects);
    }

    @SafeVarargs
    static <T> boolean areAnyNull(T... objects) {
        return areAny(NullableFunctions::isNull, objects);
    }

    static boolean areNotBlank(String... strings) {
        return areAll(StringUtils::isNotBlank, strings);
    }

    static boolean areAnyBlank(String... strings) {
        return areAny(StringUtils::isBlank, strings);
    }

    static <T> Object[] toSingleElementArray(T object, Predicate<T> guard) {
        if (!guard.test(object)) {
            return CoreConstants.EMPTY_OBJECT_ARRAY;
        }

        final var parameters = new Object[1];
        parameters[0] = object;

        return parameters;
    }

    static <T> Object[] toSingleElementArray(T object) {
        return toSingleElementArray(object, NullableFunctions::isNotNull);
    }

    static boolean castToBoolean(Object o) {
        return o instanceof Boolean ? (boolean)o : NullableFunctions.isNotNull(o);
    }

    static Boolean isStringMatchesPattern(String string, String regex) {
        return Pattern.matches(regex, string);
    }


    static String getIncrementalUUID(AtomicInteger counter) {
        return "" + counter.getAndIncrement() + UUID.randomUUID().toString();
    }

    static boolean isAllDone(ExecutionStateData stateData, int length, int index, int indicesLength) {
        if (
            NullableFunctions.isNull(stateData) ||
            areAny(BasicPredicateFunctions::isNegative, length, index, indicesLength) ||
            BasicPredicateFunctions.isSmallerThan(length, indicesLength) ||
            BasicPredicateFunctions.isSmallerThan(length, index)
        ) {
            return false;
        }

        final var executionMap = stateData.executionMap;
        final var mapNull = NullableFunctions.isNull(executionMap);
        final var indicesRemain = BasicPredicateFunctions.isPositiveNonZero(indicesLength);
        if (mapNull || indicesRemain) {
            return false;
        }

        final var mapFilled = SizableFunctions.isSizeEqualTo(executionMap::size, length);
        final var areValid = areAll(DataValidators::isValidNonFalse, executionMap.values().toArray(new Data[0]));
        return (mapFilled && areValid);
    }

    static boolean isAnyDone(ExecutionStateData stateData, int length, int index, int indicesLength) {
        if (
            NullableFunctions.isNull(stateData) ||
            areAny(BasicPredicateFunctions::isNegative, length, index, indicesLength) ||
            BasicPredicateFunctions.isSmallerThan(length, indicesLength) ||
            BasicPredicateFunctions.isSmallerThan(length, index)
        ) {
            return false;
        }

        final var executionMap = stateData.executionMap;
        final var mapNull = NullableFunctions.isNull(executionMap);
        final var indicesRemain = BasicPredicateFunctions.isPositiveNonZero(indicesLength);
        if (mapNull || indicesRemain) {
            return false;
        }

        final var mapSize = executionMap.size();
        final var mapFilled = SizableFunctions.isSizeEqualTo(mapSize, length);
        final var areValid = DataValidators.isValidNonFalse(executionMap.values().toArray(new Data[0])[mapSize-1]);
        return (mapFilled && areValid);
    }

    static QuadPredicate<ExecutionStateData, Integer, Integer, Integer> isSomeDone(int expected) {
        return (stateData, length, index, indicesLength) -> CoreUtilities.isAnyDone(stateData, length, index, indicesLength) && ((length - indicesLength) == expected);
    }
}

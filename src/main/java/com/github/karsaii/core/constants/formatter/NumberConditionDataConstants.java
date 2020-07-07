package com.github.karsaii.core.constants.formatter;

import com.github.karsaii.core.extensions.namespaces.predicates.BasicPredicates;
import com.github.karsaii.core.extensions.namespaces.predicates.SizablePredicates;
import com.github.karsaii.core.namespaces.factories.NumberConditionDataFactory;
import com.github.karsaii.core.records.formatter.NumberConditionData;

public abstract class NumberConditionDataConstants {
    public static final NumberConditionData EQUAL_TO = NumberConditionDataFactory.getWithDefaultParameterName("isEqualToExpected", "equal to", SizablePredicates::isSizeEqualTo);
    public static final NumberConditionData LESS_THAN = NumberConditionDataFactory.getWithDefaultParameterName("isLessThanExpected", "less than", BasicPredicates::isSmallerThan);
    public static final NumberConditionData MORE_THAN = NumberConditionDataFactory.getWithDefaultParameterName("isMoreThanExpected", "more than", BasicPredicates::isBiggerThan);
}

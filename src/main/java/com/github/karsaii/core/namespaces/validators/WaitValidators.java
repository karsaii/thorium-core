package com.github.karsaii.core.namespaces.validators;

import com.github.karsaii.core.namespaces.validators.wait.WaitDataValidators;
import com.github.karsaii.core.records.wait.WaitData;
import com.github.karsaii.core.records.wait.WaitTimeData;
import com.github.karsaii.core.constants.validators.CoreFormatterConstants;

import java.util.function.Function;
import java.util.function.Predicate;

import static org.apache.commons.lang3.StringUtils.isBlank;

public interface WaitValidators {
    static <T, U, V> String isValidWaitParameters(T dependency, WaitData<T, U, V> data) {
        return CoreFormatter.getNamedErrorMessageOrEmpty(
            "validateUntilParameters",
            (CoreFormatter.isNullMessageWithName(dependency, "Dependency") + WaitDataValidators.isValidWaitData(data))
        );
    }
}

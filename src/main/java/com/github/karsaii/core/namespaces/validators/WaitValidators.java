package com.github.karsaii.core.namespaces.validators;

import com.github.karsaii.core.namespaces.validators.wait.WaitDataValidators;
import com.github.karsaii.core.records.wait.WaitData;
import com.github.karsaii.core.records.wait.WaitTimeData;
import com.github.karsaii.core.constants.validators.CoreFormatterConstants;
import com.github.karsaii.core.records.wait.tasks.VoidWaitData;

import java.util.function.Function;
import java.util.function.Predicate;

import static org.apache.commons.lang3.StringUtils.isBlank;

public interface WaitValidators {
    static <DependencyType, U, ReturnType> String isValidWaitParameters(DependencyType dependency, WaitData<DependencyType, U, ReturnType> data) {
        return CoreFormatter.getNamedErrorMessageOrEmpty(
            "isValidWaitParameters",
            CoreFormatter.isNullMessageWithName(dependency, "Dependency") + WaitDataValidators.isValidWaitData(data)
        );
    }

    static <T, U, ReturnType> String isValidWaitParameters(VoidWaitData<U, ReturnType> data) {
        return CoreFormatter.getNamedErrorMessageOrEmpty(
            "isValidVoidWaitParameters",
            WaitDataValidators.isValidWaitData(data)
        );
    }
}

package com.neathorium.core.namespaces.validators;

import com.neathorium.core.namespaces.validators.wait.WaitDataValidators;
import com.neathorium.core.records.wait.WaitData;
import com.neathorium.core.records.wait.tasks.VoidWaitData;

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

package com.github.karsaii.core.namespaces;

import com.github.karsaii.core.extensions.interfaces.functional.boilers.DataSupplier;
import com.github.karsaii.core.namespaces.executor.step.StepFactory;
import com.github.karsaii.core.records.Data;

public interface DataSupplierExecutionFunctions {
    static <ReturnType> DataSupplier<ReturnType> ifDependency(String nameof, boolean condition, DataSupplier<ReturnType> positive, Data<ReturnType> negative) {
        return StepFactory.step(
            DataExecutionFunctions.ifDependency(nameof, condition, positive.getInterfaceIdentity(), negative)
        );
    }

    static <ReturnType> DataSupplier<ReturnType> ifDependency(String nameof, String errorMessage, DataSupplier<ReturnType> positive, Data<ReturnType> negative) {
        return StepFactory.step(
            DataExecutionFunctions.ifDependency(nameof, errorMessage, positive.getInterfaceIdentity(), negative)
        );
    }
}

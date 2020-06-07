package com.github.karsaii.core.records.reflection;

import com.github.karsaii.core.extensions.interfaces.functional.boilers.MethodFunction;
import com.github.karsaii.core.records.Data;
import com.github.karsaii.core.records.HandleResultData;
import com.github.karsaii.core.records.caster.BasicCastData;
import com.github.karsaii.core.abstracts.reflection.BaseInvokerDefaultsData;

import java.util.function.Function;
import java.util.function.Predicate;

public class ParameterizedInvokerDefaultsData<ParameterType, ReturnType> extends BaseInvokerDefaultsData<ParameterType, InvokerParameterizedParametersFieldData<ParameterType>, ReturnType> {
    public ParameterizedInvokerDefaultsData(
        Function<InvokerParameterizedParametersFieldData<ParameterType>, MethodFunction<Function<ParameterType, Object>>> constructor,
        Predicate<InvokerParameterizedParametersFieldData<ParameterType>> guard,
        BasicCastData<ReturnType> castData,
        Function<HandleResultData<ParameterType, ReturnType>, Data<ReturnType>> castHandler
    ) {
        super(constructor, guard, castData, castHandler);
    }
}

package com.neathorium.core.records.reflection;

import com.neathorium.core.extensions.interfaces.functional.boilers.MethodFunction;
import com.neathorium.core.records.Data;
import com.neathorium.core.records.HandleResultData;
import com.neathorium.core.records.caster.BasicCastData;
import com.neathorium.core.abstracts.reflection.BaseInvokerDefaultsData;

import java.lang.reflect.Method;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class RegularInvokerDefaultsData<ParameterType, ReturnType> extends BaseInvokerDefaultsData<ParameterType, BiFunction<Method, ParameterType, Object>, ReturnType> {
    public RegularInvokerDefaultsData(
        Function<BiFunction<Method, ParameterType, Object>, MethodFunction<Function<ParameterType, Object>>> constructor,
        Predicate<BiFunction<Method, ParameterType, Object>> guard,
        BasicCastData<ReturnType> castData,
        Function<HandleResultData<ParameterType, ReturnType>, Data<ReturnType>> castHandler) {
        super(constructor, guard, castData, castHandler);
    }
}

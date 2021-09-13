package com.neathorium.core.abstracts.reflection;

import com.neathorium.core.extensions.interfaces.functional.boilers.MethodFunction;
import com.neathorium.core.records.Data;
import com.neathorium.core.records.HandleResultData;
import com.neathorium.core.records.caster.BasicCastData;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class BaseInvokerDefaultsData<ParameterType, HandlerType, ReturnType> {
    public final Function<HandlerType, MethodFunction<Function<ParameterType, Object>>> constructor;
    public final Predicate<HandlerType> guard;
    public final BasicCastData<ReturnType> castData;
    public final Function<HandleResultData<ParameterType, ReturnType>, Data<ReturnType>> castHandler;

    public BaseInvokerDefaultsData(
        Function<HandlerType, MethodFunction<Function<ParameterType, Object>>> constructor,
        Predicate<HandlerType> guard,
        BasicCastData<ReturnType> castData,
        Function<HandleResultData<ParameterType, ReturnType>, Data<ReturnType>> castHandler
    ) {
        this.constructor = constructor;
        this.guard = guard;
        this.castData = castData;
        this.castHandler = castHandler;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var that = (BaseInvokerDefaultsData<?, ?, ?>) o;
        return Objects.equals(constructor, that.constructor) && Objects.equals(guard, that.guard) && Objects.equals(castData, that.castData) && Objects.equals(castHandler, that.castHandler);
    }

    @Override
    public int hashCode() {
        return Objects.hash(constructor, guard, castData, castHandler);
    }
}

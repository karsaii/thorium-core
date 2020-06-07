package com.github.karsaii.core.namespaces;

import com.github.karsaii.core.exceptions.MethodInvokeException;
import com.github.karsaii.core.constants.validators.CoreFormatterConstants;
import com.github.karsaii.core.constants.CoreConstants;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.github.karsaii.core.extensions.namespaces.CoreUtilities.areAnyNull;
import static com.github.karsaii.core.extensions.namespaces.CoreUtilities.areNotNull;

public interface InvokeFunctions {
    static <ParameterType> Object invoke(Method method, ParameterType base) {
        if (areAnyNull(method, base)) {
            throw new MethodInvokeException("Parameters were wrong" + CoreFormatterConstants.END_LINE);
        }

        try {
            return method.invoke(base);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            throw new MethodInvokeException(ex);
        }
    }

    static <ParameterType> Object invokeWithParameters(Method method, ParameterType base, Object[] parameters) {
        if (areAnyNull(method, base)) {
            throw new MethodInvokeException("Parameters were wrong" + CoreFormatterConstants.END_LINE);
        }

        try {
            return method.invoke(base, parameters);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            throw new MethodInvokeException(ex);
        }
    }

    static <ParameterType> Function<ParameterType, Object> regularApply(Method method, BiFunction<Method, ParameterType, Object> handler) {
        return areNotNull(method, handler) ? base -> handler.apply(method, base) : regularDefault();
    }

    static <ParameterType> Function<ParameterType, Object> defaultApplyGetter(Object object) {
        return parameter -> object;
    }

    static <ParameterType> Function<ParameterType, Object> regularDefault() {
        return defaultApplyGetter(CoreConstants.STOCK_OBJECT);
    }
}

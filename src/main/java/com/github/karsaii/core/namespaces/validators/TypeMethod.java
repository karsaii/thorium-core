package com.github.karsaii.core.namespaces.validators;

import java.lang.reflect.Method;
import java.util.Objects;

import static com.github.karsaii.core.extensions.namespaces.CoreUtilities.areNotNull;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public interface TypeMethod {
    static boolean isTypeMethod(Method method, String expectedMethodName, Class<?> clazz) {
        return (
            areNotNull(method, clazz) &&
            isNotBlank(expectedMethodName) &&
            Objects.equals(method.getName(), expectedMethodName) &&
            clazz.isAssignableFrom(method.getReturnType())
        );
    }

    static boolean isBooleanMethod(Method method, String methodName) {
        return isTypeMethod(method, methodName, Boolean.class) || isTypeMethod(method, methodName, boolean.class);
    }

    static boolean isStringMethod(Method method, String methodName) {
        return isTypeMethod(method, methodName, String.class);
    }

    static boolean isVoidMethod(Method method, String methodName) {
        return isTypeMethod(method, methodName, void.class);
    }
}

package com.github.karsaii.core.namespaces.repositories;

import com.github.karsaii.core.constants.CoreDataConstants;
import com.github.karsaii.core.namespaces.DataFactoryFunctions;
import com.github.karsaii.core.namespaces.validators.MethodRepositoryValidators;
import com.github.karsaii.core.records.Data;
import com.github.karsaii.core.records.MethodData;
import com.github.karsaii.core.records.MethodSourceData;
import com.github.karsaii.core.records.MethodParametersData;
import com.github.karsaii.core.constants.validators.CoreFormatterConstants;
import com.github.karsaii.core.namespaces.validators.CoreFormatter;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;

import static com.github.karsaii.core.namespaces.DataFactoryFunctions.replaceMessage;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public interface MethodRepository {
    private static Data<Boolean> putMethodToMap(HashMap<String, MethodData> methodMap, String methodName, Method method) {
        final var nameof = "putMethodToMap";
        if (methodMap.containsKey(methodName)) {
            return replaceMessage(CoreDataConstants.NULL_BOOLEAN, nameof, CoreFormatterConstants.METHOD_ALREADY_IN_MAP);
        }

        final var status = true;
        method.setAccessible(status);
        methodMap.put(methodName, new MethodData(method, Arrays.asList(method.getParameterTypes()).toString(), method.getReturnType().toGenericString()));

        return DataFactoryFunctions.getBoolean(status, nameof, CoreFormatterConstants.METHOD_PUT_IN_MAP);
    }

    private static Data<MethodData> getMethodFromMap(HashMap<String, MethodData> methodMap, String methodName, Data<MethodData> defaultValue) {
        final var status = methodMap.containsKey(methodName);
        return DataFactoryFunctions.getWith(
            status ? methodMap.get(methodName) : defaultValue.object,
            status,
            "getMethodFromMap",
            CoreFormatter.getMethodFromMapMessage(methodName, status)
        );
    }

    private static Data<MethodData> getMethodFromMap(HashMap<String, MethodData> methodMap, MethodParametersData parameterData) {
        return getMethodFromMap(methodMap, parameterData.methodName, CoreDataConstants.NULL_METHODDATA);
    }

    private static Data<MethodData> getMethodFromList(MethodSourceData data, MethodParametersData parameterData) {
        final var nameof = "getMethodFromList";
        final var methodName = parameterData.methodName;
        final var validator = parameterData.validator;
        final var list = data.list;
        final var methodMap = data.methodMap;
        var statusData = CoreDataConstants.NULL_BOOLEAN;
        for (var method : list) {
            if (!validator.test(method, methodName)) {
                continue;
            }

            statusData = putMethodToMap(methodMap, methodName, method);
            break;
        }

        final var methodData = getMethodFromMap(methodMap, methodName, data.defaultValue);
        final var status = statusData.status && methodData.status;
        return DataFactoryFunctions.getWith(methodData.object, status, nameof, CoreFormatter.getMethodFromListMessage(methodName, status));
    }

    static Data<MethodData> getMethod(MethodSourceData data, MethodParametersData parameterData) {
        final var nameof = "getMethod";
        final var message = MethodRepositoryValidators.validateGetMethodFromList(data, parameterData);
        final var defaultValue = data.defaultValue;
        if (isNotBlank(message)) {
            return replaceMessage(defaultValue, nameof, message);
        }

        final var methodData = getMethodFromMap(data.methodMap, parameterData.methodName, data.defaultValue);
        return methodData.status ? methodData : getMethodFromList(data, parameterData);
    }
}

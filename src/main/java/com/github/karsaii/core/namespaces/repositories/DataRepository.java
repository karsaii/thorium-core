package com.github.karsaii.core.namespaces.repositories;

import com.github.karsaii.core.records.TypedEnumKeyData;
import com.github.karsaii.core.records.Data;
import com.github.karsaii.core.enums.TypeKey;

import java.util.Map;

public interface DataRepository {
    @SuppressWarnings("unchecked")
    static <T> Data<T> get(Map<TypeKey, Data<?>> dataMap, TypedEnumKeyData<T> keyData) {
        return (Data<T>) dataMap.get(keyData.key);
    }
}

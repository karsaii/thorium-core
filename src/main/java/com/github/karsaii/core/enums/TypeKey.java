package com.github.karsaii.core.enums;

import java.util.HashMap;
import java.util.Map;

public enum TypeKey {
    BOOLEAN("Boolean"),
    INTEGER("Integer"),
    STRING("String"),
    OBJECT("Object"),
    VOID("Void"),
    WEB_ELEMENT("WebElement"),
    WEB_ELEMENT_LIST("WebElementList"),
    STRING_SET("StringSet"),
    DEFAULT("Void");

    private static final Map<String, TypeKey> VALUES = new HashMap<>();
    private String name;

    TypeKey(String name) {
        this.name = name;
    }

    static {
        for(var getter : values()) {
            VALUES.putIfAbsent(getter.name, getter);
        }
    }
    public String getName() {
        return name;
    }

    public static TypeKey getValueOf(String name) {
        return VALUES.getOrDefault(name, TypeKey.VOID);
    }
}

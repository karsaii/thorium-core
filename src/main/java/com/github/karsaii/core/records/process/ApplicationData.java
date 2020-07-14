package com.github.karsaii.core.records.process;

import java.util.Objects;

public class ApplicationData {
    public final String name;
    public final String path;
    public final String arguments;

    public ApplicationData(String name, String path, String arguments) {
        this.name = name;
        this.path = path;
        this.arguments = arguments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final var that = (ApplicationData) o;
        return Objects.equals(name, that.name) && Objects.equals(path, that.path) && Objects.equals(arguments, that.arguments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, path, arguments);
    }
}

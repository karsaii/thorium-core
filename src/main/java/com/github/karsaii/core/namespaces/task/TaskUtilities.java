package com.github.karsaii.core.namespaces.task;

import com.github.karsaii.core.extensions.interfaces.functional.boilers.DataSupplier;
import com.github.karsaii.core.records.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface TaskUtilities {
    static List<CompletableFuture<? extends Data<?>>> addToList(List<CompletableFuture<? extends Data<?>>> list, DataSupplier<?> step) {
        list.add(CompletableFuture.supplyAsync(step.getSupplier()));
        return list;
    }

    static List<CompletableFuture<? extends Data<?>>> getTaskList(DataSupplier<?> first, DataSupplier<?> second) {
        final var tasks = new ArrayList<CompletableFuture<? extends Data<?>>>();
        addToList(tasks, first);
        addToList(tasks, second);

        return tasks;
    }

    static List<CompletableFuture<? extends Data<?>>> getTaskList(DataSupplier<?> first, DataSupplier<?> second, DataSupplier<?> third) {
        final var tasks = getTaskList(first, second);
        return addToList(tasks, third);
    }


    static List<CompletableFuture<? extends Data<?>>> getTaskList(DataSupplier<?>... steps) {
        final var tasks = new ArrayList<CompletableFuture<? extends Data<?>>>();
        var index = 0;
        final var length = steps.length;
        for (; index < length; ++index) {
            addToList(tasks, steps[index]);
        }

        return tasks;
    }

    static CompletableFuture<?>[] getTaskArray(List<CompletableFuture<? extends Data<?>>> list) {
        return list.toArray(new CompletableFuture<?>[0]);
    }
}

package com.github.karsaii.core.extensions.namespaces.concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public interface CompletableFutureExtensions {
    private static CompletableFuture<?> allOfTerminateOnFailure(Function<CompletableFuture<?>[], CompletableFuture<?>> handler, CompletableFuture<?>... tasks) {
        final var failure = new CompletableFuture<>();
        for (var task : tasks) {
            task.exceptionally(ex -> {
                failure.completeExceptionally(ex);
                return null;
            });
        }

        return CompletableFuture.anyOf(failure, handler.apply(tasks));
    }

    static CompletableFuture<?> anyOfTerminateOnFailureTimed(int duration, CompletableFuture<?>... tasks) {
        final Function<CompletableFuture<?>[], CompletableFuture<?>> anyOf = CompletableFuture::anyOf;
        return allOfTerminateOnFailure(anyOf.andThen(task -> task.orTimeout(duration, TimeUnit.MILLISECONDS)), tasks);
    }

    static CompletableFuture<?> allOfTerminateOnFailureTimed(int duration, CompletableFuture<?>... tasks) {
        final Function<CompletableFuture<?>[], CompletableFuture<?>> allOf = CompletableFuture::allOf;
        return allOfTerminateOnFailure(allOf.andThen(task -> task.orTimeout(duration, TimeUnit.MILLISECONDS)), tasks);
    }

    static CompletableFuture<?> anyOfTerminateOnFailure(Function<CompletableFuture<?>[], CompletableFuture<?>> handler, CompletableFuture<?>... tasks) {
        return allOfTerminateOnFailure(CompletableFuture::anyOf, tasks);
    }

    static CompletableFuture<?> allOfTerminateOnFailure(CompletableFuture<?>... tasks) {
        return allOfTerminateOnFailure(CompletableFuture::allOf, tasks);
    }
}

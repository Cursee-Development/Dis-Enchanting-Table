package com.cursee.disenchanting_table.core.util;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface NonNullFunction<T, R> {
    @NotNull R apply(@NotNull T var1);
}

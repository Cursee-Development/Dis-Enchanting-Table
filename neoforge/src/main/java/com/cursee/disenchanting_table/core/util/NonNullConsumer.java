package com.cursee.disenchanting_table.core.util;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface NonNullConsumer<T> {
    void accept(@NotNull T var1);
}

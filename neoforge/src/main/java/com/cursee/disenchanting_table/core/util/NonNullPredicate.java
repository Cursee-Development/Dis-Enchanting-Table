package com.cursee.disenchanting_table.core.util;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface NonNullPredicate<T> {
    boolean test(@NotNull T var1);
}

package com.cursee.disenchanting_table.core.util;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface NonNullSupplier<T> {
    @NotNull T get();
}

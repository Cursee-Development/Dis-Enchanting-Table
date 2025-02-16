package com.cursee.disenchanting_table.core;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class CommonConfiguredValues {

    public static final AtomicBoolean REQUIRES_EXPERIENCE = new AtomicBoolean(true);
    public static final AtomicBoolean RESET_REPAIR_COST = new AtomicBoolean(true);

    public static AtomicReference<String> POINTS_OR_LEVELS = new AtomicReference<String>("points");
    public static AtomicReference<Integer> EXPERIENCE_COST = new AtomicReference<Integer>(Math.toIntExact(25L));
}

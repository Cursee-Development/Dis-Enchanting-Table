package com.cursee.examplemod;

import net.minecraft.resources.ResourceLocation;

public class ExampleMod {

    public static void init() {}

    public static ResourceLocation identifier(String value) {
        return new ResourceLocation(Constants.MOD_ID, value);
    }
}
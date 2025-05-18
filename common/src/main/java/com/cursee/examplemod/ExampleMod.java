package com.cursee.examplemod;

import net.minecraft.resources.ResourceLocation;

public class ExampleMod {

    public static void init() {}

    public static ResourceLocation identifier(String path) {
        return new ResourceLocation(Constants.MOD_ID, path);
    }
}
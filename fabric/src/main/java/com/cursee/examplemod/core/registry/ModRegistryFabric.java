package com.cursee.examplemod.core.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;

public class ModRegistryFabric {

    public static void register() {
        ModBlocks.register(bind(BuiltInRegistries.BLOCK));
        ModItems.register(bind(BuiltInRegistries.ITEM));
    }

    private static <T> BiConsumer<T, ResourceLocation> bind(Registry<? super T> registry) {
        return (t, id) -> Registry.register(registry, id, t);
    }
}

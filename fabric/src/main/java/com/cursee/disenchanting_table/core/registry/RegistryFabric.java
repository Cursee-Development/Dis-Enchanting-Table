package com.cursee.disenchanting_table.core.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;

public class RegistryFabric {

    public static void register() {
        ModBlocks.register(bind(BuiltInRegistries.BLOCK));
        ModBlockEntities.register(bind(BuiltInRegistries.BLOCK_ENTITY_TYPE));
        ModItems.register(bind(BuiltInRegistries.ITEM));
        ModTabs.register(bind(BuiltInRegistries.CREATIVE_MODE_TAB));
    }

    private static <T> BiConsumer<T, ResourceLocation> bind(Registry<? super T> registry) {
        return (t, id) -> Registry.register(registry, id, t);
    }
}

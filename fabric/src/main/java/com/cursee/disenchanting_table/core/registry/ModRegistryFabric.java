package com.cursee.disenchanting_table.core.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ModRegistryFabric {

    public static void register() {

        bind(BuiltInRegistries.BLOCK, ModBlocks::register);
        bind(BuiltInRegistries.ITEM, ModItems::register);
        bind(BuiltInRegistries.CREATIVE_MODE_TAB, ModTabs::register);

        bind(BuiltInRegistries.BLOCK_ENTITY_TYPE, ModBlockEntities::register);
        bind(BuiltInRegistries.MENU, ModMenus::register);
    }

    private static <T> void bind(Registry<? super T> registry, Consumer<BiConsumer<T, ResourceLocation>> source) {
        source.accept((t, resourceLocation) -> {
            Registry.register(registry, resourceLocation, t);
        });
    }
}

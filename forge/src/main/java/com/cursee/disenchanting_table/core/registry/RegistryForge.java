package com.cursee.disenchanting_table.core.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class RegistryForge {

    public static void register(IEventBus modEventBus) {
        bind(Registries.BLOCK, ModBlocks::register);
        bind(Registries.BLOCK_ENTITY_TYPE, ModBlockEntities::register);
        bind(Registries.ITEM, ModItems::register);
        bind(Registries.CREATIVE_MODE_TAB, ModTabs::register);
    }

    private static <T> void bind(ResourceKey<Registry<T>> registry, Consumer<BiConsumer<T, ResourceLocation>> source) {
        FMLJavaModLoadingContext.get().getModEventBus().addListener((RegisterEvent event) -> {
            if (registry.equals(event.getRegistryKey())) {
                source.accept((t, rl) -> event.register(registry, rl, () -> t));
            }
        });
    }
}

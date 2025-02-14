package com.cursee.examplemod.core.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class RegistryNeoForge {

    private static final AtomicReference<IEventBus> MOD_EVENT_BUS = new AtomicReference<IEventBus>(null);

    public static void register(IEventBus modEventBus) {

        if (RegistryNeoForge.MOD_EVENT_BUS.get() == null) RegistryNeoForge.MOD_EVENT_BUS.set(modEventBus);

        bind(Registries.BLOCK, consumer -> {
            ModBlocks.register(consumer);
        });
        bindForItems(ModItems::register);
    }

    private static <T> void bind(ResourceKey<Registry<T>> registry, Consumer<BiConsumer<T, ResourceLocation>> source) {
        MOD_EVENT_BUS.get().addListener((RegisterEvent event) -> {
            if (registry.equals(event.getRegistryKey())) {
                source.accept((t, rl) -> event.register(registry, rl, () -> t));
            }
        });
    }

    private static final Set<Item> CREATIVE_MODE_TAB_ITEMS = new LinkedHashSet<>();
    private static void bindForItems(Consumer<BiConsumer<Item, ResourceLocation>> source) {
        MOD_EVENT_BUS.get().addListener((RegisterEvent event) -> {
            if (event.getRegistryKey().equals(Registries.ITEM)) {
                source.accept((t, rl) -> {
                    CREATIVE_MODE_TAB_ITEMS.add(t);
                    event.register(Registries.ITEM, rl, () -> t);
                });
            }
        });
    }
}

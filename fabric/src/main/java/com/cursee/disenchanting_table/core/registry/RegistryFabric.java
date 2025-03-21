package com.cursee.disenchanting_table.core.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.BiConsumer;

public class RegistryFabric {

    public static void register() {
        ModBlocks.register(bind(BuiltInRegistries.BLOCK));
        FabricBlockEntities.register(bind(BuiltInRegistries.BLOCK_ENTITY_TYPE));
        ModItems.register(boundForItem);
        ModTabs.register(bind(BuiltInRegistries.CREATIVE_MODE_TAB));

        ModMenus.register(bind(BuiltInRegistries.MENU));
    }

    private static <T> BiConsumer<T, ResourceLocation> bind(Registry<? super T> registry) {
        return (t, id) -> Registry.register(registry, id, t);
    }

    private static final Set<Item> CREATIVE_MODE_TAB_ITEMS = new LinkedHashSet<>();
    private static final BiConsumer<Item, ResourceLocation> boundForItem = (t, id) -> {
        CREATIVE_MODE_TAB_ITEMS.add(t);
        Registry.register(BuiltInRegistries.ITEM, id, t);
    };
}

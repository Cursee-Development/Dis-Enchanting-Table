package com.cursee.disenchanting_table.core.registry;

import com.cursee.disenchanting_table.platform.Services;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.function.BiConsumer;

public class ModItems {

    // must be registered if defined
    // public static final Item EXAMPLE_BLOCK_ITEM = new BlockItem(ModBlocks.EXAMPLE_BLOCK, new Item.Properties()); // if we want the BlockItem separate

    public static void register(BiConsumer<Item, ResourceLocation> consumer) {

        if (Services.PLATFORM.isDevelopmentEnvironment()) {
            // consumer.accept(EXAMPLE_BLOCK_ITEM, BuiltInRegistries.BLOCK.getKey(ModBlocks.EXAMPLE_BLOCK)); // if we want the BlockItem separate
            // consumer.accept(new BlockItem(ModBlocks.EXAMPLE_BLOCK, new Item.Properties()), BuiltInRegistries.BLOCK.getKey(ModBlocks.EXAMPLE_BLOCK));
        }
    }
}

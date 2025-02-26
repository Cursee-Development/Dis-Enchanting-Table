package com.cursee.disenchanting_table.core.registry;

import com.cursee.disenchanting_table.Constants;
import com.cursee.disenchanting_table.DisEnchantingTable;
import com.cursee.disenchanting_table.platform.Services;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

import java.util.function.BiConsumer;

public class ModItems {

    public static final Item DISENCHANTING_TABLE = new BlockItem(ModBlocks.DISENCHANTING_TABLE, new Item.Properties());

    public static void register(BiConsumer<Item, ResourceLocation> consumer) {
        consumer.accept(DISENCHANTING_TABLE, DisEnchantingTable.identifier(Constants.MOD_ID));
    }
}

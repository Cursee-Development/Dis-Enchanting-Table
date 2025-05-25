package com.cursee.disenchanting_table.core.registry;

import com.cursee.disenchanting_table.Constants;
import com.cursee.disenchanting_table.DisenchantingTable;
import com.cursee.disenchanting_table.core.world.block.DisenchantingTableBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.function.BiConsumer;

public class ModBlocks {

    public static final Block DISENCHANTING_TABLE = new DisenchantingTableBlock();

    public static void register(BiConsumer<Block, ResourceLocation> consumer) {
        consumer.accept(DISENCHANTING_TABLE, DisenchantingTable.identifier(Constants.MOD_ID));
    }
}

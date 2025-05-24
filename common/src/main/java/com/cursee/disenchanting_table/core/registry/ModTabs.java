package com.cursee.disenchanting_table.core.registry;

import com.cursee.disenchanting_table.Constants;
import com.cursee.disenchanting_table.DisenchantingTable;
import com.cursee.disenchanting_table.platform.Services;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.BiConsumer;

public class ModTabs {

    public static final CreativeModeTab DISENCHANTING_TABLE = Services.PLATFORM.creativeModeTab(() -> new ItemStack(ModBlocks.DISENCHANTING_TABLE), Component.translatable("itemGroup.disenchantingTable"), (itemDisplayParameters, output) -> output.accept(ModBlocks.DISENCHANTING_TABLE));

    public static void register(BiConsumer<CreativeModeTab, ResourceLocation> consumer) {
        consumer.accept(DISENCHANTING_TABLE, DisenchantingTable.identifier(Constants.MOD_ID));
    }
}

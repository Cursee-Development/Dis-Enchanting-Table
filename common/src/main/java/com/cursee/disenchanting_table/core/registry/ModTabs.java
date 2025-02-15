package com.cursee.disenchanting_table.core.registry;

import com.cursee.disenchanting_table.DisEnchantingTable;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.BiConsumer;

public class ModTabs {

    public static final CreativeModeTab DISENCHANTING_TAB = CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .icon(() -> new ItemStack(ModBlocks.DISENCHANTING_TABLE))
            .title(Component.translatable("itemGroup.disenchantingTable"))
            .displayItems((itemDisplayParameters, output) -> {
                output.accept(ModBlocks.DISENCHANTING_TABLE);
                output.accept(ModBlocks.HOPPER_DISENCHANTING_TABLE);
            })
            .build();

    public static void register(BiConsumer<CreativeModeTab, ResourceLocation> consumer) {
        consumer.accept(DISENCHANTING_TAB, DisEnchantingTable.identifier("disenchanting_tab"));
    }
}

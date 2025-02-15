package com.cursee.disenchanting_table.core.registry;

import com.cursee.disenchanting_table.DisEnchantingTable;
import com.cursee.disenchanting_table.core.world.block.entity.HopperDisEnchantingTableBlockEntity;
import com.cursee.disenchanting_table.platform.Services;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.BiConsumer;

public class ModBlockEntities {

    public static final BlockEntityType<HopperDisEnchantingTableBlockEntity> HOPPER_DISENCHANTING_TABLE = Services.PLATFORM.createBlockEntityType(HopperDisEnchantingTableBlockEntity::new, ModBlocks.HOPPER_DISENCHANTING_TABLE);

    public static void register(BiConsumer<BlockEntityType<?>, ResourceLocation> consumer) {
        consumer.accept(HOPPER_DISENCHANTING_TABLE, DisEnchantingTable.identifier("hopper_disenchanting_table"));
    }
}

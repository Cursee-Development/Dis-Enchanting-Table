package com.cursee.disenchanting_table.core.registry;

import com.cursee.disenchanting_table.Constants;
import com.cursee.disenchanting_table.DisenchantingTable;
import com.cursee.disenchanting_table.core.world.block.entity.DisenchantingTableBlockEntity;
import com.cursee.disenchanting_table.platform.Services;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Set;
import java.util.function.BiConsumer;

public class ModBlockEntities {

    public static final BlockEntityType<DisenchantingTableBlockEntity> DISENCHANTING_TABLE = Services.PLATFORM.blockEntityType(DisenchantingTableBlockEntity::new, Set.of(ModBlocks.DISENCHANTING_TABLE));

    public static void register(BiConsumer<BlockEntityType<?>, ResourceLocation> consumer) {
        consumer.accept(DISENCHANTING_TABLE, DisenchantingTable.identifier(Constants.MOD_ID));
    }
}

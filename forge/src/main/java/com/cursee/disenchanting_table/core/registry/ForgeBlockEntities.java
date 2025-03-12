package com.cursee.disenchanting_table.core.registry;

import com.cursee.disenchanting_table.Constants;
import com.cursee.disenchanting_table.DisEnchantingTable;
import com.cursee.disenchanting_table.core.world.block.entity.ForgeDisEnchantingBE;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.BiConsumer;

public class ForgeBlockEntities {

    public static final BlockEntityType<ForgeDisEnchantingBE> DISENCHANTING_TABLE = BlockEntityType.Builder.of(ForgeDisEnchantingBE::new, ModBlocks.DISENCHANTING_TABLE).build(null);

    public static void register(BiConsumer<BlockEntityType<?>, ResourceLocation> consumer) {
        consumer.accept(DISENCHANTING_TABLE, DisEnchantingTable.identifier(Constants.MOD_ID));
    }
}

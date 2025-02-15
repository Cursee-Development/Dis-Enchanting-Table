package com.cursee.disenchanting_table.core.world.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class HopperDisEnchantingTableBlockEntity extends BlockEntity {

    public HopperDisEnchantingTableBlockEntity(BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
}

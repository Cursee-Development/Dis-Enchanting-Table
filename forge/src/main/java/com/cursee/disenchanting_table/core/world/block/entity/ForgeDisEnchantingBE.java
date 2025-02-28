package com.cursee.disenchanting_table.core.world.block.entity;

import com.cursee.disenchanting_table.core.registry.ForgeBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ForgeDisEnchantingBE extends BlockEntity {

    public ForgeDisEnchantingBE(BlockPos pos, BlockState state) {
        this(ForgeBlockEntities.DISENCHANTING_TABLE, pos, state);
    }

    public ForgeDisEnchantingBE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }
}

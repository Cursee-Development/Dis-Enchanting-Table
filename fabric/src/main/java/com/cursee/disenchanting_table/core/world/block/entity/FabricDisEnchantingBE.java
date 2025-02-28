package com.cursee.disenchanting_table.core.world.block.entity;

import com.cursee.disenchanting_table.core.registry.FabricBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class FabricDisEnchantingBE extends BlockEntity {

    public FabricDisEnchantingBE(BlockPos pos, BlockState state) {
        this(FabricBlockEntities.DISENCHANTING_TABLE, pos, state);
    }

    public FabricDisEnchantingBE(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public void doTick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {

    }
}

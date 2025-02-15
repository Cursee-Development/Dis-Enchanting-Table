package com.cursee.disenchanting_table.core.world.block;

import com.cursee.disenchanting_table.Constants;
import com.cursee.disenchanting_table.core.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class HopperDisEnchantingTableBlock extends Block implements EntityBlock {

    public HopperDisEnchantingTableBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {

        if (!level.isClientSide() && level.getBlockEntity(pos) instanceof MenuProvider menuProvider) player.openMenu(menuProvider);

        return InteractionResult.SUCCESS;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return ModBlockEntities.HOPPER_DISENCHANTING_TABLE.create(blockPos, blockState);
    }
}

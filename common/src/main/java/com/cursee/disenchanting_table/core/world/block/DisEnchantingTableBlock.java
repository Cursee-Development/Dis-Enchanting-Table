package com.cursee.disenchanting_table.core.world.block;

import com.cursee.disenchanting_table.client.gui.menu.DisEnchantingTableMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class DisEnchantingTableBlock extends Block {

    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D);

    public DisEnchantingTableBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState $$0, BlockGetter $$1, BlockPos $$2, CollisionContext $$3) {
        return DisEnchantingTableBlock.SHAPE;
    }

    @Override
    public boolean isPathfindable(BlockState $$0, BlockGetter $$1, BlockPos $$2, PathComputationType $$3) {
        return false;
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        for(int i = 0; i < 3; ++i) {
            int j = randomSource.nextInt(2) * 2 - 1;
            int k = randomSource.nextInt(2) * 2 - 1;
            double d = (double)blockPos.getX() + 0.5 + 0.25 * (double)j;
            double e = (double)((float)blockPos.getY() + randomSource.nextFloat());
            double f = (double)blockPos.getZ() + 0.5 + 0.25 * (double)k;
            double g = (double)(randomSource.nextFloat() * (float)j);
            double h = ((double)randomSource.nextFloat() - 0.5) * 0.125;
            double l = (double)(randomSource.nextFloat() * (float)k);
            level.addParticle(ParticleTypes.PORTAL, d, e, f, g, h, l);
        }
    }

    private static DisEnchantingTableMenu createMenu(int containerID, Inventory inventory, Level level, BlockPos pos) {
        return new DisEnchantingTableMenu(containerID, inventory, ContainerLevelAccess.create(level, pos));
    }

    @Override
    public @Nullable MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return new SimpleMenuProvider(
            (containerID, inventory, player) -> createMenu(containerID, inventory, level, pos),
            Component.translatable("itemGroup.disenchantingTable")
        );
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {

        if (!level.isClientSide()) player.openMenu(state.getMenuProvider(level, pos));

        return InteractionResult.SUCCESS;
    }
}

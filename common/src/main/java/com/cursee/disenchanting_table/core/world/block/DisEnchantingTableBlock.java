package com.cursee.disenchanting_table.core.world.block;

import com.cursee.disenchanting_table.core.CommonConfigValues;
import com.cursee.disenchanting_table.core.world.inventory.ManualDisenchantingMenu;
import com.cursee.disenchanting_table.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DisEnchantingTableBlock extends Block implements EntityBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D);

    public DisEnchantingTableBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public boolean isPathfindable(BlockState $$0, BlockGetter $$1, BlockPos $$2, PathComputationType $$3) {
        return false;
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult result) {
        if (level.isClientSide) return InteractionResult.SUCCESS;
        else if (level.getBlockEntity(pos) instanceof MenuProvider menuProvider) {
            if (CommonConfigValues.automatic_disenchanting) player.openMenu(menuProvider);
            else player.openMenu(new SimpleMenuProvider((i, inventory, player1) -> new ManualDisenchantingMenu(i, inventory, ContainerLevelAccess.create(level, pos)), Component.translatable("block.disenchanting_table.disenchanting_table")));
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return null;
        // Services.PLATFORM.createLoaderDisenchantingTableBE(pos, state);
    }

    private @Nullable static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> serverType, BlockEntityType<E> clientType, BlockEntityTicker<? super E> ticker) {
        return clientType == serverType ? (BlockEntityTicker<A>)ticker : null;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return null; // todo finish getTicker
        // level.isClientSide() ? null : createTickerHelper(type, Services.PLATFORM.getLoaderDisenchantingTableBEType(), Services.PLATFORM::doLoaderDisenchantingTableBETick);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {

        // todo create client config option to disable animateTick

        for(int i = 0; i < 3; ++i) {
            int xMod = random.nextInt(2) * 2 - 1;
            int zMod = random.nextInt(2) * 2 - 1;
            double xPos = (double)pos.getX() + 0.5D + 0.25D * (double)xMod;
            double yPos = (double)((float)pos.getY() + random.nextFloat());
            double zPos = (double)pos.getZ() + 0.5D + 0.25D * (double)zMod;
            double xSpeed = (double)(random.nextFloat() * (float)xMod);
            double ySpeed = ((double)random.nextFloat() - 0.5D) * 0.125D;
            double zSpeed = (double)(random.nextFloat() * (float)zMod);
            level.addParticle(ParticleTypes.PORTAL, xPos, yPos, zPos, xSpeed, ySpeed, zSpeed);
        }
    }
}

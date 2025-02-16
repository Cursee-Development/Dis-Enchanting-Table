package com.cursee.disenchanting_table.core.world.block;

import com.cursee.disenchanting_table.core.registry.ModBlockEntities;
import com.cursee.disenchanting_table.core.world.block.entity.HopperDisEnchantingTableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class HopperDisEnchantingTableBlock extends BaseEntityBlock {

    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public HopperDisEnchantingTableBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter level, BlockPos blockPos, CollisionContext context) {
        return HopperDisEnchantingTableBlock.SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public boolean isPathfindable(BlockState $$0, BlockGetter $$1, BlockPos $$2, PathComputationType $$3) {
        return false;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return ModBlockEntities.HOPPER_DISENCHANTING_TABLE.create(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
        return level.isClientSide() ? null : createTickerHelper(type, ModBlockEntities.HOPPER_DISENCHANTING_TABLE, (levelX, blockPos, blockStateX, disenchantingTable) -> disenchantingTable.serverTick(levelX, blockPos, blockStateX));
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult result) {

        if (!level.isClientSide() && level.getBlockEntity(blockPos) instanceof MenuProvider menuProvider) player.openMenu(menuProvider);

        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newBlockState, boolean movedByPiston) {
        if (blockState.getBlock() != newBlockState.getBlock()) {

            BlockEntity blockEntity = level.getBlockEntity(blockPos);

            if (blockEntity instanceof HopperDisEnchantingTableBlockEntity hopperDisenchantingTable) {
                Containers.dropContents(level, blockPos, hopperDisenchantingTable);
                level.updateNeighbourForOutputSignal(blockPos, this);
            }

            super.onRemove(blockState, level, blockPos, newBlockState, movedByPiston);
        }
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        for(int particleCount = 0; particleCount < 3; ++particleCount) {
            int xModifier = randomSource.nextInt(2) * 2 - 1;
            int zModifier = randomSource.nextInt(2) * 2 - 1;
            double xPos = (double)blockPos.getX() + 0.5 + 0.25 * (double)xModifier;
            double yPos = (double)((float)blockPos.getY() + randomSource.nextFloat());
            double zPos = (double)blockPos.getZ() + 0.5 + 0.25 * (double)zModifier;
            double xSpeed = (double)(randomSource.nextFloat() * (float)xModifier);
            double ySpeed = ((double)randomSource.nextFloat() - 0.5) * 0.125;
            double zSpeed = (double)(randomSource.nextFloat() * (float)zModifier);
            level.addParticle(ParticleTypes.PORTAL, xPos, yPos, zPos, xSpeed, ySpeed, zSpeed);
        }
    }
}

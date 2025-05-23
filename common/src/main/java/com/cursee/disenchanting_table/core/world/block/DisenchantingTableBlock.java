package com.cursee.disenchanting_table.core.world.block;

import com.cursee.disenchanting_table.client.ClientConfig;
import com.cursee.disenchanting_table.core.registry.ModBlockEntities;
import com.cursee.disenchanting_table.core.world.block.entity.DisenchantingTableBlockEntity;
import com.cursee.disenchanting_table.core.world.block.entity.util.SimpleInventoryBlockEntity;
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
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DisenchantingTableBlock extends Block implements EntityBlock {

    private static final VoxelShape SHAPE;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public DisenchantingTableBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.ENCHANTING_TABLE));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult result) {
        if (level.isClientSide) return InteractionResult.SUCCESS;
        else if (level.getBlockEntity(pos) instanceof MenuProvider menuProvider) player.openMenu(menuProvider);
        return InteractionResult.SUCCESS;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new DisenchantingTableBlockEntity(blockPos, blockState);
    }

    @SuppressWarnings("unchecked")
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> serverType, BlockEntityType<E> clientType, BlockEntityTicker<? super E> ticker) {
        return clientType == serverType ? (BlockEntityTicker<A>) ticker : null;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? createTickerHelper(blockEntityType, ModBlockEntities.DISENCHANTING_TABLE, DisenchantingTableBlockEntity::tickClient) : createTickerHelper(blockEntityType, ModBlockEntities.DISENCHANTING_TABLE, DisenchantingTableBlockEntity::tickServer);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
        BlockEntity block = world.getBlockEntity(pos);
        if (block instanceof DisenchantingTableBlockEntity table) {
            return table.getSignal();
        }
        return 0;
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!newState.is(state.getBlock())) {
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof SimpleInventoryBlockEntity inventory) {
                Containers.dropContents(world, pos, inventory.getItemHandler());
            }
            super.onRemove(state, world, pos, newState, isMoving);
        }
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
    public @Nonnull BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public @Nonnull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {

        if (!ClientConfig.render_block_particles) return;

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

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    static {
        SHAPE = Block.box(0, 0, 0, 16, 12, 16);
    }
}

package com.cursee.disenchanting_table.core.world.block.entity;

import com.cursee.disenchanting_table.core.CommonConfigValues;
import com.cursee.disenchanting_table.core.network.packet.ForgeConfigSyncS2CPacket;
import com.cursee.disenchanting_table.core.network.packet.ForgeItemSyncS2CPacket;
import com.cursee.disenchanting_table.core.registry.ForgeBlockEntities;
import com.cursee.disenchanting_table.core.registry.ForgeNetwork;
import com.cursee.disenchanting_table.core.util.DisenchantmentHelper;
import com.cursee.disenchanting_table.core.world.block.DisEnchantingTableBlock;
import com.cursee.disenchanting_table.core.world.inventory.AutoDisEnchantingMenu;
import com.cursee.disenchanting_table.core.world.inventory.ManualDisenchantingMenu;
import com.cursee.disenchanting_table.platform.Services;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ForgeDisEnchantingBE extends BlockEntity implements MenuProvider, Container {

    private ItemStackHandler itemHandler = new DisenchantingTableItemStackHandler();
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private final Map<Direction, LazyOptional<WrappedHandler>> directionWrappedHandlerMap =
            new InventoryDirectionWrapper(itemHandler,
                    new InventoryDirectionEntry(Direction.DOWN, 2, false),
                    new InventoryDirectionEntry(Direction.NORTH, 1, CommonConfigValues.automatic_disenchanting),
                    new InventoryDirectionEntry(Direction.SOUTH, 1, CommonConfigValues.automatic_disenchanting),
                    new InventoryDirectionEntry(Direction.EAST, 1, CommonConfigValues.automatic_disenchanting),
                    new InventoryDirectionEntry(Direction.WEST, 1, CommonConfigValues.automatic_disenchanting),
                    new InventoryDirectionEntry(Direction.UP, 0, CommonConfigValues.automatic_disenchanting)).directionsMap;

    private int progress = 0;
    private final ContainerData containerData;

    public ForgeDisEnchantingBE(BlockPos pos, BlockState state) {
        this(ForgeBlockEntities.DISENCHANTING_TABLE, pos, state);
    }

    public ForgeDisEnchantingBE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        containerData = new DisenchantingTableContainerData();
    }

    public void doTick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {

        if (level == null) return;

        boolean validInputs = DisenchantmentHelper.canRemoveEnchantments(this.getItem(0)) && this.getItem(1).is(Items.BOOK);
        if (validInputs && getItem(2).isEmpty() && nearestPlayerHasEnoughExperience(level, pos)) {
            this.progress += 1;
            BlockEntity.setChanged(level, pos, state);

            if (this.progress >= 10) {
                this.disenchant(level, pos);
                this.setChanged();
                this.progress = 0;
            }
        }
        else this.progress = 0;
    }

    @Override
    protected void saveAdditional(CompoundTag data) {
        data.put("inventory", itemHandler.serializeNBT());
        data.putInt("progress", this.progress);
        super.saveAdditional(data);
    }

    @Override
    public void load(CompoundTag data) {
        super.load(data);
        itemHandler.deserializeNBT(data.getCompound("inventory"));
        this.progress = data.getInt("progress");
    }

    @Override
    public void setChanged() {
        if(level != null && !level.isClientSide()) {

            NonNullList<ItemStack> inventory = NonNullList.withSize(3, ItemStack.EMPTY);
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                inventory.set(i, itemHandler.getStackInSlot(i));
            }

            for(Player player : level.players()) { // todo item sync
                if (player instanceof ServerPlayer serverPlayer) ForgeNetwork.sendToPlayer(new ForgeItemSyncS2CPacket(inventory, getBlockPos()), serverPlayer);
            }
        }

        super.setChanged();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.disenchanting_table.disenchanting_table");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerIndex, Inventory playerInventory, Player player) {

        if (!CommonConfigValues.automatic_disenchanting) {
            return new ManualDisenchantingMenu(containerIndex, playerInventory, ContainerLevelAccess.create(level, this.getBlockPos()));
        }

        return new AutoDisEnchantingMenu(containerIndex, playerInventory, this, containerData);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    public int getContainerSize() {
        return 3;
    } // container

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ItemStack getItem(int pSlot) {
        return itemHandler.getStackInSlot(pSlot);
    }

    @Override
    public ItemStack removeItem(int pSlot, int pAmount) {
        return itemHandler.extractItem(pSlot, pAmount, false);
    }

    @Override
    public ItemStack removeItemNoUpdate(int pSlot) {
        return itemHandler.extractItem(pSlot, itemHandler.getStackInSlot(pSlot).getCount(), true);
    }

    @Override
    public void setItem(int pSlot, ItemStack pStack) {
        itemHandler.setStackInSlot(pSlot, pStack);
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            if (!itemHandler.getStackInSlot(i).isEmpty()) {
                setItem(i, ItemStack.EMPTY);
            }
        }
    } // container

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            if(side == null) {
                return lazyItemHandler.cast();
            }

            if(directionWrappedHandlerMap.containsKey(side)) {
                Direction localDir = getBlockState().getValue(DisEnchantingTableBlock.FACING);

                if(side == Direction.DOWN ||side == Direction.UP) {
                    return directionWrappedHandlerMap.get(side).cast();
                }

                return switch (localDir) {
                    default -> directionWrappedHandlerMap.get(side.getOpposite()).cast();
                    case EAST -> directionWrappedHandlerMap.get(side.getClockWise()).cast();
                    case SOUTH -> directionWrappedHandlerMap.get(side).cast();
                    case WEST -> directionWrappedHandlerMap.get(side.getCounterClockWise()).cast();
                };
            }
        }

        return super.getCapability(cap, side);
    }

    private @Nullable Enchantment keptEnchantment;
    private @Nullable Integer keptEnchantmentLevel;
    private @Nullable Map<Enchantment, Integer> stolenEnchantments;
    private void disenchant(Level level, BlockPos pos) {

        ItemStack input = this.getItem(0);

        if (!input.is(Items.ENCHANTED_BOOK)) {
            this.stolenEnchantments = EnchantmentHelper.getEnchantments(input);
            ItemStack result = new ItemStack(Items.ENCHANTED_BOOK);
            EnchantmentHelper.setEnchantments(this.stolenEnchantments, result);
            this.setItem(2, result);

            input.setRepairCost(0);
            EnchantmentHelper.setEnchantments(EnchantmentHelper.getEnchantments(ItemStack.EMPTY), input);
            this.setItem(0, input);

            ItemStack extra = this.getItem(1);
            extra.shrink(1);
            this.setItem(1, extra);
        }
        else {
            this.stolenEnchantments = EnchantmentHelper.getEnchantments(input);
            this.keptEnchantment = this.stolenEnchantments.keySet().iterator().next();
            this.keptEnchantmentLevel = this.stolenEnchantments.get(this.keptEnchantment);
            this.stolenEnchantments.remove(this.keptEnchantment);

            ItemStack result = new ItemStack(Items.ENCHANTED_BOOK);
            EnchantmentHelper.setEnchantments(this.stolenEnchantments, result);
            this.setItem(2, result);

            if (this.keptEnchantment == null || this.keptEnchantmentLevel == null) return;
            ItemStack keptEnchantedBook = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(this.keptEnchantment, this.keptEnchantmentLevel));
            this.setItem(0, keptEnchantedBook);

            ItemStack bookStack = this.getItem(1);
            bookStack.shrink(1);
            this.setItem(1, bookStack);
        }

        Player player = this.nearestPlayer(level, pos);
        if (!CommonConfigValues.requires_experience || player == null || player.getAbilities().instabuild) return; // player is never null here due to preceding nearestPlayerHasEnoughExperience
        if (CommonConfigValues.uses_points) {
            if (player.totalExperience >= CommonConfigValues.experience_cost) player.giveExperiencePoints(-CommonConfigValues.experience_cost);
            else {
                player.experienceLevel -= 1;
                int newXP = player.getXpNeededForNextLevel();
                newXP -= CommonConfigValues.experience_cost;
                player.giveExperiencePoints(newXP);
            }
        }
        else {
            player.giveExperienceLevels(-CommonConfigValues.experience_cost);
        }
    }

    public void setInventory(NonNullList<ItemStack> list) {
        for(int i = 0; i < list.size(); i++) {
            this.itemHandler.setStackInSlot(i, list.get(i));
        }
    }

    public ItemStack getRenderStack() {
        return !getItem(2).isEmpty() ? getItem(2) : getItem(0);
    }

    private @Nullable Player nearestPlayer(Level level, BlockPos pos) {
        return level.getNearestPlayer(TargetingConditions.forNonCombat(), pos.getX(), pos.getY(), pos.getZ());
    }

    private boolean nearestPlayerHasEnoughExperience(Level level, BlockPos pos) {

        Player player = this.nearestPlayer(level, pos);

        if (player == null) return false;
        if (!CommonConfigValues.requires_experience || player.experienceLevel > 0 || player.getAbilities().instabuild) return true;

        return false;
    }

    public class DisenchantingTableContainerData implements ContainerData {

        @Override
        public int get(int index) {
            return index == 0 ? progress : 0;
        }

        @Override
        public void set(int index, int value) {
            if (index == 0) progress = value;
        }

        @Override
        public int getCount() {
            return 1;
        }
    }

    public class DisenchantingTableItemStackHandler extends ItemStackHandler {

        public DisenchantingTableItemStackHandler() {
            super(3); // todo fix magic value
        }

        @Override
        protected void onContentsChanged(int slot) {
            ForgeDisEnchantingBE.this.setChanged();
            if(level != null && !level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
            }
        }

        // used for can place item?
        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return switch (slot) {
                case 0 -> DisenchantmentHelper.canRemoveEnchantments(stack);
                case 1 -> stack.is(Items.BOOK);
                default -> false;
            };
        }
    }
}

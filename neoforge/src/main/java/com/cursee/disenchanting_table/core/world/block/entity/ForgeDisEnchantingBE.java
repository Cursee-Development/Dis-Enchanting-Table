package com.cursee.disenchanting_table.core.world.block.entity;

import com.cursee.disenchanting_table.core.CommonConfigValues;
import com.cursee.disenchanting_table.core.network.packet.NeoForgeItemSyncS2CPacket;
import com.cursee.disenchanting_table.core.registry.ForgeBlockEntities;
import com.cursee.disenchanting_table.core.registry.ForgeNetwork;
import com.cursee.disenchanting_table.core.util.DisenchantmentHelper;
import com.cursee.disenchanting_table.core.util.LazyOptional;
import com.cursee.disenchanting_table.core.world.block.DisEnchantingTableBlock;
import com.cursee.disenchanting_table.core.world.inventory.AutoDisEnchantingMenu;
import com.cursee.disenchanting_table.core.world.inventory.ManualDisenchantingMenu;
import net.minecraft.core.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
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
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
//import net.minecraftforge.common.capabilities.Capability;
//import net.minecraftforge.common.capabilities.ForgeCapabilities;
//import net.minecraftforge.common.util.LazyOptional;
//import net.minecraftforge.items.IItemHandler;
//import net.minecraftforge.items.ItemStackHandler;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.IBlockCapabilityProvider;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ForgeDisEnchantingBE extends BlockEntity implements MenuProvider, WorldlyContainer, IBlockCapabilityProvider {

    private ItemStackHandler itemHandler = new DisenchantingTableItemStackHandler();
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private final Map<Direction, LazyOptional<WrappedHandler>> directionWrappedHandlerMap =
            new InventoryDirectionWrapper((IItemHandlerModifiable) itemHandler,
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

//    @Override
//    protected void saveAdditional(CompoundTag data) {
//        data.put("inventory", itemHandler.serializeNBT());
//        data.putInt("progress", this.progress);
//        super.saveAdditional(data);
//    }


    @Override
    protected void saveAdditional(CompoundTag data, HolderLookup.Provider registries) {
        data.put("inventory", itemHandler.serializeNBT(registries));
        data.putInt("progress", this.progress);
        super.saveAdditional(data, registries);
    }

//    @Override
//    public void load(CompoundTag data) {
//        super.load(data);
//        if (CommonConfigValues.automatic_disenchanting) itemHandler.deserializeNBT(data.getCompound("inventory"));
//        this.progress = data.getInt("progress");
//    }


    @Override
    protected void loadAdditional(CompoundTag data, HolderLookup.Provider registries) {
        super.loadAdditional(data, registries);
        if (CommonConfigValues.automatic_disenchanting) itemHandler.deserializeNBT(registries, data.getCompound("inventory"));
        this.progress = data.getInt("progress");
    }

    @Override
    public void setChanged() {
        if(level != null && !level.isClientSide()) {

            NonNullList<ItemStack> inventory = NonNullList.withSize(3, ItemStack.EMPTY);
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                inventory.set(i, itemHandler.getStackInSlot(i));
            }

            for(Player player : level.players()) {
                if (player instanceof ServerPlayer serverPlayer) {
                    // ForgeNetwork.sendToPlayer(new ForgeItemSyncS2CPacket(getBlockPos(), inventory.size(), inventory), serverPlayer);
                    PacketDistributor.sendToPlayer(serverPlayer, new NeoForgeItemSyncS2CPacket(getBlockPos().asLong(), inventory.size(), new Holder.Direct<Item>(inventory.get(2).getItem())));
                }
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

//    @Override
//    public @NotNull CompoundTag getUpdateTag() {
//        return saveWithoutMetadata();
//    }


    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

//    @Override
//    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
//        super.onDataPacket(net, pkt);
//    }


    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        super.onDataPacket(net, pkt, lookupProvider);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

//    @Override
//    public void invalidateCaps() {
//        super.invalidateCaps();
//        lazyItemHandler.invalidate();
//    }


    @Override
    public void invalidateCapabilities() {
        super.invalidateCapabilities();
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

//    @Override
//    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
//        if(cap == ForgeCapabilities.ITEM_HANDLER) {
//            if(side == null) {
//                return lazyItemHandler.cast();
//            }
//
//            if(directionWrappedHandlerMap.containsKey(side)) {
//                Direction localDir = getBlockState().getValue(DisEnchantingTableBlock.FACING);
//
//                if(side == Direction.DOWN ||side == Direction.UP) {
//                    return directionWrappedHandlerMap.get(side).cast();
//                }
//
//                return switch (localDir) {
//                    default -> directionWrappedHandlerMap.get(side.getOpposite()).cast();
//                    case EAST -> directionWrappedHandlerMap.get(side.getClockWise()).cast();
//                    case SOUTH -> directionWrappedHandlerMap.get(side).cast();
//                    case WEST -> directionWrappedHandlerMap.get(side.getCounterClockWise()).cast();
//                };
//            }
//        }
//
//        return super.getCapability(cap, side);
//    }


    @Override
    public @Nullable Object getCapability(Level level, BlockPos blockPos, BlockState blockState, @Nullable BlockEntity blockEntity, Object o) {
        ResourceLocation rl = BuiltInRegistries.BLOCK.getKey(level.getBlockState(blockPos).getBlock());
        return BlockCapability.createSided(rl, ItemStackHandler.class);
    } // todo fix this? lol

    private @Nullable Holder<Enchantment> keptEnchantment;
    private @Nullable Integer keptEnchantmentLevel;
    private @Nullable ItemEnchantments stolenEnchantments = ItemEnchantments.EMPTY;
    private void disenchant(Level level, BlockPos pos) {

        ItemStack input = this.getItem(0);

        if (!input.is(Items.ENCHANTED_BOOK)) {
            this.stolenEnchantments = EnchantmentHelper.getEnchantmentsForCrafting(input);//EnchantmentHelper.getEnchantments(input);
            ItemStack result = new ItemStack(Items.ENCHANTED_BOOK);
            // EnchantmentHelper.setEnchantments(this.stolenEnchantments, result);
            EnchantmentHelper.setEnchantments(result, this.stolenEnchantments);
            this.setItem(2, result);

            // input.setRepairCost(0);
            if (CommonConfigValues.resets_repair_cost) input.set(DataComponents.REPAIR_COST, 0);
            // EnchantmentHelper.setEnchantments(EnchantmentHelper.getEnchantments(ItemStack.EMPTY), input);
            EnchantmentHelper.setEnchantments(input, ItemEnchantments.EMPTY);
            this.setItem(0, input);

            ItemStack extra = this.getItem(1);
            extra.shrink(1);
            this.setItem(1, extra);
        }
        else {
            this.stolenEnchantments = EnchantmentHelper.getEnchantmentsForCrafting(input);
            this.keptEnchantment = this.stolenEnchantments.keySet().stream().findFirst().get();
            this.keptEnchantmentLevel = this.stolenEnchantments.getLevel(this.keptEnchantment);

            // this.stolenEnchantments.remove(this.keptEnchantment);
            ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(this.stolenEnchantments);
            mutable.removeIf(enchantmentHolder -> enchantmentHolder.value() == this.keptEnchantment.value());
            this.stolenEnchantments = mutable.toImmutable();

            ItemStack result = new ItemStack(Items.ENCHANTED_BOOK);
            // EnchantmentHelper.setEnchantments(this.stolenEnchantments, result);
            EnchantmentHelper.setEnchantments(result, this.stolenEnchantments);
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

    @Override
    public int @NotNull [] getSlotsForFace(@NotNull Direction side) {
        return side == Direction.DOWN ? new int[]{2} : new int[]{0, 1};
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction side) {
        return CommonConfigValues.automatic_disenchanting && (side != Direction.DOWN && canPlaceItem(slot, stack));
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack pStack, Direction side) {
        return CommonConfigValues.automatic_disenchanting && (side == Direction.DOWN && slot == 2);
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

    public class DisenchantingTableItemStackHandler extends ItemStackHandler implements IItemHandlerModifiable {

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
                case 0 -> CommonConfigValues.automatic_disenchanting && DisenchantmentHelper.canRemoveEnchantments(stack);
                case 1 -> CommonConfigValues.automatic_disenchanting && stack.is(Items.BOOK);
                default -> false;
            };
        }
    }
}

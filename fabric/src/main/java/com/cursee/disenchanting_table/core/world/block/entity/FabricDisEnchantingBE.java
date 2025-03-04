package com.cursee.disenchanting_table.core.world.block.entity;

import com.cursee.disenchanting_table.core.CommonConfigValues;
import com.cursee.disenchanting_table.core.network.packet.FabricItemSyncS2CPacket;
import com.cursee.disenchanting_table.core.registry.FabricBlockEntities;
import com.cursee.disenchanting_table.core.registry.FabricNetwork;
import com.cursee.disenchanting_table.core.util.DisenchantmentHelper;
import com.cursee.disenchanting_table.core.world.inventory.AutoDisEnchantingMenu;
import com.cursee.disenchanting_table.core.world.inventory.ManualDisenchantingMenu;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class FabricDisEnchantingBE extends BlockEntity implements MenuProvider, IContainer {

    private int progress = 0;
    private final ContainerData containerData;
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(3, ItemStack.EMPTY);

    public FabricDisEnchantingBE(BlockPos pos, BlockState state) {
        this(FabricBlockEntities.DISENCHANTING_TABLE, pos, state);
    }

    public FabricDisEnchantingBE(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        containerData = new DisenchantingTableContainerData();
    }

    public void doTick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {

        if (level == null) return;

        boolean validInputs = DisenchantmentHelper.canRemoveEnchantments(this.getItem(0)) && this.getItem(1).is(Items.BOOK);
        if (validInputs && getItem(2).isEmpty() && this.nearestPlayerHasEnoughExperience(level, pos)) {
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
    public NonNullList<ItemStack> getItems() {
        return inventory;
    }

    public void setInventory(NonNullList<ItemStack> list) {
        for(int i = 0; i < list.size(); i++) {
            this.inventory.set(i, list.get(i));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag data) {
        ContainerHelper.saveAllItems(data, inventory);
        data.putInt("progress", this.progress);
        super.saveAdditional(data);
    }

    @Override
    public void load(CompoundTag data) {
        super.load(data);
        ContainerHelper.loadAllItems(data, inventory);
        this.progress = data.getInt("progress");
    }

    @Override
    public void setChanged() {
        if(level != null && !level.isClientSide()) {
            for(Player player : level.players()) {
                if (player instanceof ServerPlayer serverPlayer) FabricItemSyncS2CPacket.registerS2CPacketSender(serverPlayer, inventory, getBlockPos());
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
    public void setRemoved() {

        super.setRemoved();
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction side) {
        return CommonConfigValues.automatic_disenchanting && (side != Direction.DOWN && canPlaceItem(slot, stack));
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) {
        return CommonConfigValues.automatic_disenchanting && (side == Direction.DOWN && slot == 2);
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return (index == 0 && DisenchantmentHelper.canRemoveEnchantments(stack)) || (index == 1 && stack.is(Items.BOOK));
    }

    @Override
    public boolean canTakeItem(Container target, int index, ItemStack stack) {
        return index == 2;
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
}

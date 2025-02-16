package com.cursee.disenchanting_table.core.world.block.entity;

import com.cursee.disenchanting_table.core.CommonConfiguredValues;
import com.cursee.disenchanting_table.core.world.inventory.HopperDisEnchantingTableMenu;
import com.cursee.disenchanting_table.core.registry.ModBlockEntities;
import com.cursee.disenchanting_table.core.registry.ModMessages;
import com.cursee.disenchanting_table.platform.Services;
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
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class HopperDisEnchantingTableBlockEntity extends BaseContainerBlockEntity implements MenuProvider, IContainer {

    private final NonNullList<ItemStack> inventory = NonNullList.withSize(3, ItemStack.EMPTY);
    private final ContainerData containerData;

    private int progress = 0;
    private int maxProgress = 10;

    public HopperDisEnchantingTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.HOPPER_DISENCHANTING_TABLE, pos, state);
        this.containerData = new HopperDisenchantingTableData();
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
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
    public Component getDisplayName() {
        return Component.literal("Hopper Dis-Enchanting Table");
    }

    /** @see net.minecraft.world.level.block.entity.BrewingStandBlockEntity#createMenu(int, Inventory, Player) */
    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerID, Inventory inventory, Player player) {
        return new HopperDisEnchantingTableMenu(containerID, inventory, this, this.containerData);
    }

    /** From BaseContainerBlockEntity */
    @Override
    protected Component getDefaultName() {
        return Component.literal("Hopper Dis-Enchanting Table");
    }

    /** From BaseContainerBlockEntity */
    @Override
    protected AbstractContainerMenu createMenu(int containerID, Inventory inventory) {
        return new HopperDisEnchantingTableMenu(containerID, inventory, this, this.containerData);
    }

    @Override
    protected void saveAdditional(CompoundTag data) {
        ContainerHelper.saveAllItems(data, this.inventory);
        data.putInt("progress", this.progress);
        super.saveAdditional(data);
    }

    @Override
    public void load(CompoundTag data) {
        super.load(data);
        progress = data.getInt("progress");
        ContainerHelper.loadAllItems(data, inventory);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    private boolean validInput() {
        ItemStack input = this.getItem(0);
        ItemStack additional = this.getItem(1);

        boolean inputEnchanted = !EnchantmentHelper.getEnchantments(input).isEmpty();
        boolean inputValidItem = inputEnchanted && !input.is(Items.ENCHANTED_BOOK);
        boolean inputValidBook = inputEnchanted && EnchantedBookItem.getEnchantments(input).size() > 1;
        boolean additionalBookFound = additional.is(Items.BOOK);

        return (inputValidItem || inputValidBook) && additionalBookFound;
    }

    private boolean outputEmpty() {
        return this.getItem(2).isEmpty();
    }

    private @Nullable Player nearestPlayer(Level level, BlockPos pos) {
        return level.getNearestPlayer(TargetingConditions.forNonCombat(), pos.getX(), pos.getY(), pos.getZ());
    }

    private boolean nearestPlayerHasEnoughExperience(Level level, BlockPos pos) {

        Player player = this.nearestPlayer(level, pos);

        if (player == null) return false;
        if (player.experienceLevel > 0 || player.getAbilities().instabuild) return true; // todo fix hard-coded value

        return false;
    }

    private @Nullable Enchantment keptEnchantment;
    private @Nullable Integer keptEnchantmentLevel;
    private @Nullable Map<Enchantment, Integer> stolenEnchantments;
    private void disenchant(Level level, BlockPos pos) {

        ItemStack input = this.getItem(0);

        if (input.is(Items.ENCHANTED_BOOK)) {
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
        else {
            this.stolenEnchantments = EnchantmentHelper.getEnchantments(input);
            ItemStack result = new ItemStack(Items.ENCHANTED_BOOK);
            EnchantmentHelper.setEnchantments(this.stolenEnchantments, result);
            this.setItem(2, result);

            EnchantmentHelper.setEnchantments(EnchantmentHelper.getEnchantments(ItemStack.EMPTY), input);
            if (CommonConfiguredValues.RESET_REPAIR_COST.get()) input.setRepairCost(0);
            this.setItem(0, input);

            ItemStack bookStack = this.getItem(1);
            bookStack.shrink(1);
            this.setItem(1, bookStack);
        }

        Player player = this.nearestPlayer(level, pos);
        if (player == null || player.getAbilities().instabuild) return; // player is never null here due to preceding nearestPlayerHasEnoughExperience
        if (CommonConfiguredValues.POINTS_OR_LEVELS.get().equals("points")) {
            if (player.totalExperience >= CommonConfiguredValues.EXPERIENCE_COST.get()) player.giveExperiencePoints(-CommonConfiguredValues.EXPERIENCE_COST.get());
            else {
                player.experienceLevel -= 1;
                int newXP = player.getXpNeededForNextLevel();
                newXP -= CommonConfiguredValues.EXPERIENCE_COST.get();
                player.giveExperiencePoints(newXP);
            }
        }
        else if (CommonConfiguredValues.POINTS_OR_LEVELS.get().equals("levels")) {
            player.giveExperienceLevels(-CommonConfiguredValues.EXPERIENCE_COST.get());
        }
    }

    public void serverTick(Level level, BlockPos pos, BlockState state) {

        if (level == null) return;

        if (level.getGameTime() % 10 == 0 && this.validInput()) {
            BlockEntity.setChanged(level, pos, state);
            setChanged();
        }

        if (this.validInput() && this.outputEmpty() && this.nearestPlayerHasEnoughExperience(level, pos)) {

            this.progress += 1;
            BlockEntity.setChanged(level, pos, state); // make sure the block entity is always saved to disk

            if (this.progress >= this.maxProgress) {
                this.disenchant(level, pos);
                this.setChanged();
                this.progress = 0;
            }
        }
        else this.progress = 0;
    }

    public ItemStack getRenderStack() {
        return this.getItem(0);
    }

    @Override
    public void setChanged() {
        if(level != null && !level.isClientSide()) {

            FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer());

            data.writeInt(inventory.size());
            for (ItemStack stack : inventory) {
                data.writeItem(stack);
            }

            data.writeBlockPos(getBlockPos());

            for(Player player : level.players()) {
                if (player instanceof ServerPlayer serverPlayer) Services.PLATFORM.sendToPlayer(serverPlayer, ModMessages.ITEM_SYNC_S2C, data);
            }
        }

        super.setChanged();
    }

    public class HopperDisenchantingTableData implements ContainerData {

        @Override
        public int get(int dataIndex) {
            return switch (dataIndex) {
                case 0 -> HopperDisEnchantingTableBlockEntity.this.progress;
                case 1 -> HopperDisEnchantingTableBlockEntity.this.maxProgress;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> HopperDisEnchantingTableBlockEntity.this.progress = value;
                case 1 -> HopperDisEnchantingTableBlockEntity.this.maxProgress = value;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    private static final int[] SLOTS_FOR_UP = new int[]{0}; // input enchanted item from the top
    private static final int[] SLOTS_FOR_SIDES = new int[]{1}; // input normal books from the sides
    private static final int[] SLOTS_FOR_DOWN = new int[]{0, 2}; // output disenchanted items and enchanted books from the bottom
    @Override
    public int[] getSlotsForFace(Direction side) {
        return side == Direction.UP ? SLOTS_FOR_UP : side == Direction.DOWN ? SLOTS_FOR_DOWN : SLOTS_FOR_SIDES;
    }

    @Override
    public boolean canPlaceItem(int slotIndex, ItemStack stack) {

        boolean inputEnchanted = !EnchantmentHelper.getEnchantments(stack).isEmpty();
        boolean inputValidItem = inputEnchanted && !stack.is(Items.ENCHANTED_BOOK);
        boolean inputValidBook = inputEnchanted && EnchantedBookItem.getEnchantments(stack).size() > 1;

        if (slotIndex == 0) return inputValidItem || inputValidBook;
        if (slotIndex == 1) return stack.is(Items.BOOK);

        return false;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction side) {
        return this.canPlaceItem(slot, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) {

        if (slot == 0) {
            if (stack.is(Items.ENCHANTED_BOOK) && EnchantedBookItem.getEnchantments(stack).size() < 2) return true;
            else if (EnchantmentHelper.getEnchantments(stack).isEmpty()) return true;
        }

        if (slot == 1) return false;

        return slot == 2;
    }
}

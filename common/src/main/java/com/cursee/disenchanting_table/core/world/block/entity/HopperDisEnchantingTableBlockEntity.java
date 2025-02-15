package com.cursee.disenchanting_table.core.world.block.entity;

import com.cursee.disenchanting_table.client.gui.menu.HopperDisEnchantingTableMenu;
import com.cursee.disenchanting_table.core.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class HopperDisEnchantingTableBlockEntity extends BlockEntity implements MenuProvider, IContainer {

    private final NonNullList<ItemStack> inventory = NonNullList.withSize(3, ItemStack.EMPTY);
    private final ContainerData containerData;

    private int progress = 0;
    private int maxProgress = 10;

    public HopperDisEnchantingTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.HOPPER_DISENCHANTING_TABLE, pos, state);
        this.containerData = new HopperDisenchantingTableData();
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return inventory;
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

    public void serverTick(Level level, BlockPos pos, BlockState state) {
        if (level == null || level.getGameTime() % 20 != 0) return;
        this.progress++;
        if (this.progress >= this.maxProgress) {
            this.progress = 0;
        }
    }

    private class HopperDisenchantingTableData implements ContainerData {

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
}

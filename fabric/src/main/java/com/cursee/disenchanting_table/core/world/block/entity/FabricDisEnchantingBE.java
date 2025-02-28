package com.cursee.disenchanting_table.core.world.block.entity;

import com.cursee.disenchanting_table.Constants;
import com.cursee.disenchanting_table.core.CommonConfigValues;
import com.cursee.disenchanting_table.core.registry.FabricBlockEntities;
import com.cursee.disenchanting_table.core.world.inventory.AutoDisEnchantingMenu;
import com.cursee.disenchanting_table.core.world.inventory.ManualDisenchantingMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

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

    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return inventory;
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

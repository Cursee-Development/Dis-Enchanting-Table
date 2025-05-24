package com.cursee.disenchanting_table.core.world.block.entity.util;

import com.google.common.base.Preconditions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Clearable;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/// Adapted from [Botania](https://github.com/VazkiiMods/Botania)
public abstract class SimpleInventoryBlockEntity extends ModBlockEntity implements Clearable {

    private final SimpleContainer itemHandler = createItemHandler();

    protected SimpleInventoryBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        itemHandler.addListener(i -> setChanged());
    }

    public static void copyToInv(NonNullList<ItemStack> src, Container dest) {
        Preconditions.checkArgument(src.size() == dest.getContainerSize());
        for (int i = 0; i < src.size(); i++) {
            dest.setItem(i, src.get(i));
        }
    }

    public static NonNullList<ItemStack> copyFromInv(Container inv) {
        NonNullList<ItemStack> ret = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ret.set(i, inv.getItem(i));
        }
        return ret;
    }

    @Override
    public void readPacketNBT(CompoundTag tag, HolderLookup.Provider registries) {
        NonNullList<ItemStack> tmp = NonNullList.withSize(inventorySize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, tmp, registries);
        copyToInv(tmp, itemHandler);
    }

    @Override
    public void writePacketNBT(CompoundTag tag, HolderLookup.Provider registries) {
        ContainerHelper.saveAllItems(tag, copyFromInv(itemHandler), registries);
    }

    public final int inventorySize() {
        return getItemHandler().getContainerSize();
    }

    protected abstract SimpleContainer createItemHandler();

    @Override
    public void clearContent() {
        getItemHandler().clearContent();
    }

    public final Container getItemHandler() {
        return itemHandler;
    }

    public void setInventory(NonNullList<ItemStack> items) {
        for (int i = 0; i < items.size(); i++) {
            getItemHandler().setItem(i, items.get(i));
        }
    }
}

package com.cursee.disenchanting_table.core.world.block.entity;

import com.cursee.disenchanting_table.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;

public class ForgeHopperDisEnchantingTableBlockEntity extends HopperDisEnchantingTableBlockEntity {

    public ForgeHopperDisEnchantingTableBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction side) {

        boolean inputEnchanted = !EnchantmentHelper.getEnchantments(stack).isEmpty();
        boolean inputValidItem = inputEnchanted && !stack.is(Items.ENCHANTED_BOOK);
        boolean inputValidBook = inputEnchanted && EnchantedBookItem.getEnchantments(stack).size() > 1;
        boolean validForInputSlot = slot == 0 && (inputValidItem || inputValidBook);
        boolean validForAdditionalSlot = slot == 1 && stack.is(Items.BOOK) && (side != Direction.UP && side != Direction.DOWN);

        return validForInputSlot || validForAdditionalSlot;
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

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        return cap == ForgeCapabilities.ITEM_HANDLER ? LazyOptional.empty() : super.getCapability(cap, side);
    }
}

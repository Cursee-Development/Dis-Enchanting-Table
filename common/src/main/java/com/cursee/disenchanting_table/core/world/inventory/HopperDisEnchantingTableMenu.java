package com.cursee.disenchanting_table.core.world.inventory;

import com.cursee.disenchanting_table.core.registry.ModMenus;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

/** @see BrewingStandMenu */
public class HopperDisEnchantingTableMenu extends AbstractContainerMenu {

    private final Container container;
    private final ContainerData containerData;

    public HopperDisEnchantingTableMenu(int pContainerId, Inventory pPlayerInventory) {
        this(pContainerId, pPlayerInventory, new SimpleContainer(3), new SimpleContainerData(2));
    }

    public HopperDisEnchantingTableMenu(int pContainerId, Inventory pPlayerInventory, Container pContainer, ContainerData pContainerData) {
        super(ModMenus.HOPPER_DISENCHANTING_MENU, pContainerId);
        checkContainerSize(pContainer, 3);
        checkContainerDataCount(pContainerData, 2);
        this.container = pContainer;
        this.containerData = pContainerData;
        pContainer.startOpen(pPlayerInventory.player);

        this.addSlot(new Slot(pContainer, 0, 27, 47) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                if (stack.is(Items.ENCHANTED_BOOK) && EnchantedBookItem.getEnchantments(stack).size() <= 1) return false;
                return !EnchantmentHelper.getEnchantments(stack).isEmpty();
            }
        });
        this.addSlot(new Slot(pContainer, 1, 76, 47) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(Items.BOOK);
            }
        });
        this.addSlot(new Slot(pContainer, 2, 134, 47) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            @Override
            public boolean mayPickup(Player player) {
                return HopperDisEnchantingTableMenu.this.mayPickup(player, this.hasItem());
            }

            @Override
            public void onTake(Player player, ItemStack stack) {
                HopperDisEnchantingTableMenu.this.onTake(player, stack);
            }
        });

        this.addDataSlots(pContainerData);

        // y offset to begin player slots at
        final int yOffset = (-1 * 18) - 1;

        // add player inventory slots
        for(int row = 0; row < 3; ++row) {
            for(int column = 0; column < 9; ++column) {
                this.addSlot(new Slot(pPlayerInventory, column + row * 9 + 9, 8 + column * 18, 103 + row * 18 + yOffset));
            }
        }

        // add player hotbar slots
        for(int column = 0; column < 9; ++column) {
            this.addSlot(new Slot(pPlayerInventory, column, 8 + column * 18, 161 + yOffset));
        }
    }

    private void onTake(Player player, ItemStack stack) {
        // todo: add experience cost here
    }

    private boolean mayPickup(Player player, boolean hasItem) {
        // todo: add experience cost here
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {

        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        if (!slot.hasItem()) return newStack;

        ItemStack originalStack = slot.getItem();
        newStack = originalStack.copy();

        final boolean itemMovedToPlayer = pIndex < this.container.getContainerSize();
        if (itemMovedToPlayer) {
            if (!this.moveItemStackTo(originalStack, this.container.getContainerSize(), this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        }
        else if (!this.moveItemStackTo(originalStack, 0, this.container.getContainerSize(), false)) {
            return ItemStack.EMPTY;
        }

        if (originalStack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        }
        else {
            slot.setChanged();
        }

        return newStack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return this.container.stillValid(pPlayer);
    }

    public int getCraftingTicks() {
        return this.containerData.get(0);
    }
}

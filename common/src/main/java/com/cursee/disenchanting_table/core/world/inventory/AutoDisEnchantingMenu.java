package com.cursee.disenchanting_table.core.world.inventory;

import com.cursee.disenchanting_table.Constants;
import com.cursee.disenchanting_table.core.registry.ModMenus;
import com.cursee.disenchanting_table.core.util.DisenchantmentHelper;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class AutoDisEnchantingMenu extends AbstractContainerMenu {

    private final Container container;
    private final ContainerData containerData;

    public AutoDisEnchantingMenu(int containerIndex, Inventory playerInventory) {
        this(containerIndex, playerInventory, new SimpleContainer(3), new SimpleContainerData(1));
    }

    public AutoDisEnchantingMenu(int containerIndex, Inventory playerInventory, Container container, ContainerData containerData) {
        super(ModMenus.AUTO_DISENCHANTING_TABLE, containerIndex);
        this.container = container;
        this.containerData = containerData;

        this.addSlot(new Slot(container, 0, 27, 47) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return DisenchantmentHelper.canRemoveEnchantments(stack);
            }
        });
        this.addSlot(new Slot(container, 1, 76, 47) {
            @Override
            public boolean mayPlace(ItemStack $$0) {
                return $$0.is(Items.BOOK);
            }
        });
        this.addSlot(new Slot(container, 2, 134, 47) {

            @Override
            public boolean mayPlace(ItemStack $$0) {
                return false;
            }
        });

        for(int row = 0; row < 3; ++row) {
            for(int column = 0; column < 9; ++column) {
                this.addSlot(new Slot(playerInventory, column + row * 9 + 9, 8 + column * 18, 84 + row * 18));
            }
        }

        for(int column = 0; column < 9; ++column) {
            this.addSlot(new Slot(playerInventory, column, 8 + column * 18, 142));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}

package com.cursee.disenchanting_table.core.world.inventory;

import com.cursee.disenchanting_table.core.registry.ModMenus;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;

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
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return false;
    }
}

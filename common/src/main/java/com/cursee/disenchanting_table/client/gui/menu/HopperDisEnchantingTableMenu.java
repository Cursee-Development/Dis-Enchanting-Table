package com.cursee.disenchanting_table.client.gui.menu;

import com.cursee.disenchanting_table.core.registry.ModMenus;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class HopperDisEnchantingTableMenu extends AbstractContainerMenu {

    public HopperDisEnchantingTableMenu(int containerID, Inventory inventory) {
        super(ModMenus.HOPPER_DISENCHANTING_MENU, containerID);
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

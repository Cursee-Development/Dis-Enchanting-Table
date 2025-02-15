package com.cursee.disenchanting_table.client.gui.menu;

import com.cursee.disenchanting_table.core.registry.ModMenus;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class DisEnchantingTableMenu extends ItemCombinerMenu {

    public DisEnchantingTableMenu(int containerID, Inventory inventory) {
        this(containerID, inventory, ContainerLevelAccess.NULL);
    }

    public DisEnchantingTableMenu(int containerID, Inventory inventory, ContainerLevelAccess containerAccess) {
        super(ModMenus.DISENCHANTING_MENU, containerID, inventory, containerAccess);
    }

    @Override
    protected boolean mayPickup(Player player, boolean b) {
        return false;
    }

    @Override
    protected void onTake(Player player, ItemStack itemStack) {

    }

    @Override
    protected boolean isValidBlock(BlockState blockState) {
        return false;
    }

    @Override
    public void createResult() {

    }

    @Override
    protected ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
        return null;
    }
}

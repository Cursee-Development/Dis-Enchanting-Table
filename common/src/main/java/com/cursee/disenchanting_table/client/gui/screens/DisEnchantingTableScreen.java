package com.cursee.disenchanting_table.client.gui.screens;

import com.cursee.disenchanting_table.Constants;
import com.cursee.disenchanting_table.core.world.inventory.DisEnchantingTableMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class DisEnchantingTableScreen extends ItemCombinerScreen<DisEnchantingTableMenu> {

    public static final ResourceLocation BACKGROUND = new ResourceLocation(Constants.MOD_ID, "textures/gui/container/disenchanting_table.png");

    public DisEnchantingTableScreen(DisEnchantingTableMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, BACKGROUND);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelY += 9999;
        this.inventoryLabelY += 9999;
    }

    @Override
    protected void renderErrorIcon(GuiGraphics guiGraphics, int i, int i1) {

    }
}

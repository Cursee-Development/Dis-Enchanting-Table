package com.cursee.disenchanting_table.client.gui.screen;

import com.cursee.disenchanting_table.client.gui.menu.HopperDisEnchantingTableMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class HopperDisEnchantingTableScreen extends AbstractContainerScreen<HopperDisEnchantingTableMenu> {

    public HopperDisEnchantingTableScreen(HopperDisEnchantingTableMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelY += 9999;
        this.inventoryLabelY += 9999;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        guiGraphics.blit(DisEnchantingTableScreen.BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        // draw crafting progress as a string for debugging
        // guiGraphics.drawString(this.font, String.valueOf(this.menu.getCraftingTicks()), 15, 15, 0xFFFFFFFF, false);
    }
}

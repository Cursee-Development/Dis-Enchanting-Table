package com.cursee.disenchanting_table.client.gui.screen;

import com.cursee.disenchanting_table.client.gui.menu.HopperDisEnchantingTableMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class HopperDisEnchantingTableScreen extends AbstractContainerScreen<HopperDisEnchantingTableMenu> {

    public HopperDisEnchantingTableScreen(HopperDisEnchantingTableMenu $$0, Inventory $$1, Component $$2) {
        super($$0, $$1, $$2);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        guiGraphics.blit(DisEnchantingTableScreen.BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }
}

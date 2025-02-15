package com.cursee.disenchanting_table.client.gui.screen;

import com.cursee.disenchanting_table.client.gui.menu.HopperDisEnchantingTableMenu;
import com.cursee.disenchanting_table.platform.Services;
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

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(DisEnchantingTableScreen.BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        // draw crafting progress as a string for debugging
        if (Services.PLATFORM.isDevelopmentEnvironment()) guiGraphics.drawString(this.font, String.valueOf(this.menu.getCraftingTicks()), 15, 15, 0xFFFFFFFF, false);
    }
}

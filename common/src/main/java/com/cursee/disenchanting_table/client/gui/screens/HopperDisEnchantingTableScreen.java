package com.cursee.disenchanting_table.client.gui.screens;

import com.cursee.disenchanting_table.client.ClientConfiguredValues;
import com.cursee.disenchanting_table.core.world.inventory.HopperDisEnchantingTableMenu;
import com.cursee.disenchanting_table.platform.Services;
import net.minecraft.client.Minecraft;
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

        boolean mayPickupResult = this.menu.mayPickup.get() == 1;

        if (this.menu.getSlot(0).hasItem() && !mayPickupResult) {
            guiGraphics.blit(DisEnchantingTableScreen.BACKGROUND, this.leftPos + 99 + 3, this.topPos + 45, this.imageWidth, 0, 28, 21);
        }

        if (!ClientConfiguredValues.EXPERIENCE_INDICATOR.get() || mayPickupResult) return;

        Minecraft instance = Minecraft.getInstance();
        if (instance.player == null || instance.player.getAbilities().instabuild) return;

        final int textColor = 0xFFFF6060;
        final int xStart = this.leftPos + 45;
        final int yStart = this.topPos + 72;
        Component text = Component.literal("Insufficient Experience!");
        guiGraphics.fill(xStart, yStart, xStart + this.font.width(text) + 4, yStart + 11, 0xFF45327F);
        guiGraphics.drawString(this.font, text, xStart + 2, yStart + 2, textColor);
    }
}

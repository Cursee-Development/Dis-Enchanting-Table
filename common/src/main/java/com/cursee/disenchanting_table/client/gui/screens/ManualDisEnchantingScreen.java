package com.cursee.disenchanting_table.client.gui.screens;

import com.cursee.disenchanting_table.Constants;
import com.cursee.disenchanting_table.client.ClientConfigValues;
import com.cursee.disenchanting_table.core.CommonConfigValues;
import com.cursee.disenchanting_table.core.world.inventory.ManualDisenchantingMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ManualDisEnchantingScreen extends ItemCombinerScreen<ManualDisenchantingMenu> {

    public static final ResourceLocation BACKGROUND = new ResourceLocation(Constants.MOD_ID, "textures/gui/container/disenchanting_table.png");

    public ManualDisEnchantingScreen(ManualDisenchantingMenu $$0, Inventory $$1, Component $$2) {
        super($$0, $$1, $$2, BACKGROUND);
    }

    @Override
    protected void renderErrorIcon(GuiGraphics guiGraphics, int i, int i1) {
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {

        super.renderBg(guiGraphics, partialTick, mouseX, mouseY);

        boolean mayPickupResult = this.menu.mayPickup.get() == 1;
        if (!ClientConfigValues.experience_indicator || mayPickupResult) return;

        Minecraft instance = Minecraft.getInstance();
        if (instance.player == null || instance.player.getAbilities().instabuild) return;

        if (this.menu.mayPickup.get() == 1) {
            guiGraphics.blit(BACKGROUND, this.leftPos + 99 + 3, this.topPos + 45, this.imageWidth, 0, 28, 21);
        }

        final int textPadding = 4;
        final int xStart = this.leftPos + 45;
        final int yStart = this.topPos + 72;
        Component text = Component.literal("Insufficient Experience!");
        guiGraphics.fill(xStart, yStart, xStart + this.font.width(text) + textPadding, yStart + 11, 0xFF45327F);
        guiGraphics.drawString(this.font, text, xStart + (textPadding / 2), yStart + (textPadding / 2), 0xFFFF6060);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelY += 9999;
        this.inventoryLabelY += 9999;
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height) {
        this.init(minecraft, width, height);
    }
}

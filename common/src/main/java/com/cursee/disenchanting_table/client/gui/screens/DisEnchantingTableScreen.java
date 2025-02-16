package com.cursee.disenchanting_table.client.gui.screens;

import com.cursee.disenchanting_table.Constants;
import com.cursee.disenchanting_table.client.ClientConfiguredValues;
import com.cursee.disenchanting_table.core.world.inventory.DisEnchantingTableMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

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
    public void resize(Minecraft minecraft, int width, int height) {
        this.init(minecraft, width, height);
    }

    @Override
    protected void renderErrorIcon(GuiGraphics guiGraphics, int i, int i1) {
        // no-op
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {

        super.renderBg(guiGraphics, partialTick, mouseX, mouseY);

        boolean mayPickupResult = this.menu.mayPickup.get() == 1;

        if (this.menu.getSlot(0).hasItem() && !mayPickupResult) {
            guiGraphics.blit(BACKGROUND, this.leftPos + 99 + 3, this.topPos + 45, this.imageWidth, 0, 28, 21);
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

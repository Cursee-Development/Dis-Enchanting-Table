package com.cursee.disenchanting_table.client.gui.screens;

import com.cursee.disenchanting_table.DisenchantingTable;
import com.cursee.disenchanting_table.core.world.inventory.DisenchantingTableMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class DisenchantingTableScreen extends AbstractContainerScreen<DisenchantingTableMenu> {

    public static final ResourceLocation DISENCHANTING_TABLE_LOCATION = DisenchantingTable.identifier("textures/gui/container/disenchanting_table.png");

    public DisenchantingTableScreen(DisenchantingTableMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
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
    protected void renderBg(GuiGraphics guiGraphics, float v, int mouseX, int mouseY) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(DISENCHANTING_TABLE_LOCATION, i, j, 0, 0, this.imageWidth, this.imageHeight);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}

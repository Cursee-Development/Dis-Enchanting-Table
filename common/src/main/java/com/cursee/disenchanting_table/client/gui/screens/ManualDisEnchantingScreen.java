package com.cursee.disenchanting_table.client.gui.screens;

import com.cursee.disenchanting_table.Constants;
import com.cursee.disenchanting_table.core.world.inventory.AutoDisEnchantingMenu;
import com.cursee.disenchanting_table.core.world.inventory.ManualDisenchantingMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
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
    public void render(GuiGraphics guiGraphics, int $$1, int $$2, float $$3) {
        super.render(guiGraphics, $$1, $$2, $$3);
    }

    @Override
    protected void renderErrorIcon(GuiGraphics guiGraphics, int i, int i1) {
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

//    public ManualDisEnchantingScreen(ManualDisEnchantingScreen menu, Inventory playerInventory, Component title) {
//        super(menu, playerInventory, title);
//    }
//
//    @Override
//    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
//        guiGraphics.blit(BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
//    }
//
//    @Override
//    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
//        super.render(guiGraphics, mouseX, mouseY, partialTick);
//        this.renderTooltip(guiGraphics, mouseX, mouseY);
//    }
//
//    @Override
//    protected void init() {
//        super.init();
//        this.titleLabelY += 9999;
//        this.inventoryLabelY += 9999;
//    }
//
//    @Override
//    public void resize(Minecraft minecraft, int width, int height) {
//        this.init(minecraft, width, height);
//    }
}

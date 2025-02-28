package com.cursee.disenchanting_table.client.gui.screens;

import com.cursee.disenchanting_table.Constants;
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

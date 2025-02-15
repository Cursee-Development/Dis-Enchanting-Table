package com.cursee.disenchanting_table.client.gui.screen;

import com.cursee.disenchanting_table.Constants;
import com.cursee.disenchanting_table.client.gui.menu.DisEnchantingTableMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class DisEnchantingTableScreen extends ItemCombinerScreen<DisEnchantingTableMenu> {

    public static final ResourceLocation BACKGROUND = new ResourceLocation(Constants.MOD_ID, "textures/gui/container/disenchanting.png");

    public DisEnchantingTableScreen(DisEnchantingTableMenu $$0, Inventory $$1, Component $$2) {
        super($$0, $$1, $$2, BACKGROUND);
    }

    public static DisEnchantingTableScreen create(DisEnchantingTableMenu $$0, Inventory $$1, Component $$2) {
        return new DisEnchantingTableScreen($$0, $$1, $$2);
    }

    @Override
    protected void renderErrorIcon(GuiGraphics guiGraphics, int i, int i1) {

    }
}

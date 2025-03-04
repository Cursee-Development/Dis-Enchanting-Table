package com.cursee.disenchanting_table.client.gui.screens;

import com.cursee.disenchanting_table.Constants;
import com.cursee.disenchanting_table.client.ClientConfigValues;
import com.cursee.disenchanting_table.core.CommonConfigValues;
import com.cursee.disenchanting_table.core.util.ExperienceHelper;
import com.cursee.disenchanting_table.core.world.inventory.ManualDisenchantingMenu;
import com.cursee.disenchanting_table.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

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

        if (!this.menu.hasResult()) return;

        Minecraft instance = Minecraft.getInstance();
        if (instance.player == null) return;
        Player player = instance.player;

        // assume the player cannot pickup the item
        boolean mayPickupResult = false;

        // if the cost is in points, and the player has more than or equal to the cost, they can pickup the item
        if (CommonConfigValues.uses_points && ExperienceHelper.totalPointsFromLevelAndProgress(player.experienceLevel, player.experienceProgress) >= CommonConfigValues.experience_cost) mayPickupResult = true;

        // if the cost is in levels, and the player has more than or equal to the cost, they can pickup the item
        else if (!CommonConfigValues.uses_points && player.experienceLevel >= CommonConfigValues.experience_cost) mayPickupResult = true;

        // if the player can pickup the result, we don't draw the "insufficient experience" text or it's background
        if (mayPickupResult || player.getAbilities().instabuild) return;

        guiGraphics.blit(BACKGROUND, this.leftPos + 99 + 3, this.topPos + 45, this.imageWidth, 0, 28, 21);

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

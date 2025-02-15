package com.cursee.disenchanting_table;

import com.cursee.disenchanting_table.client.gui.screen.DisEnchantingTableScreen;
import com.cursee.disenchanting_table.core.registry.ModMenus;
import net.minecraft.client.gui.screens.MenuScreens;

public class DisEnchantingTableClient {

    public static void init() {
        MenuScreens.register(ModMenus.DISENCHANTING_MENU, DisEnchantingTableScreen::create);
    }
}

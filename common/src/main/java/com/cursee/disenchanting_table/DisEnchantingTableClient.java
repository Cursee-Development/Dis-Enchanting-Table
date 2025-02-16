package com.cursee.disenchanting_table;

import com.cursee.disenchanting_table.client.gui.screens.DisEnchantingTableScreen;
import com.cursee.disenchanting_table.client.gui.screens.HopperDisEnchantingTableScreen;
import com.cursee.disenchanting_table.core.registry.ModMenus;
import net.minecraft.client.gui.screens.MenuScreens;

public class DisEnchantingTableClient {

    public static void init() {
        MenuScreens.register(ModMenus.DISENCHANTING_MENU, DisEnchantingTableScreen::new);
        MenuScreens.register(ModMenus.HOPPER_DISENCHANTING_MENU, HopperDisEnchantingTableScreen::new);
    }
}

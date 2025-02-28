package com.cursee.disenchanting_table;

import com.cursee.disenchanting_table.client.gui.screens.AutoDisEnchantingScreen;
import com.cursee.disenchanting_table.client.gui.screens.ManualDisEnchantingScreen;
import com.cursee.disenchanting_table.core.registry.ModMenus;
import com.cursee.disenchanting_table.platform.Services;

public class DisEnchantingTableClient {

    public static void init() {
        Services.PLATFORM.registerScreen(ModMenus.AUTO_DISENCHANTING_TABLE, AutoDisEnchantingScreen::new);
        Services.PLATFORM.registerScreen(ModMenus.MANUAL_DISENCHANTING_TABLE, ManualDisEnchantingScreen::new);
    }
}

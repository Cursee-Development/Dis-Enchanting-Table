package com.cursee.disenchanting_table;

import com.cursee.disenchanting_table.client.ClientConfig;
import com.cursee.disenchanting_table.client.gui.screens.DisenchantingMenuScreen;
import com.cursee.disenchanting_table.client.gui.screens.DisenchantingTableScreen;
import com.cursee.disenchanting_table.core.registry.ModMenus;
import com.cursee.disenchanting_table.platform.Services;

public class DisenchantingTableClient {

    public static void init() {

        Services.PLATFORM.registerScreen(ModMenus.DISENCHANTING_TABLE, DisenchantingTableScreen::new);
        Services.PLATFORM.registerScreen(ModMenus.DISENCHANTING_MENU, DisenchantingMenuScreen::new);

        ClientConfig.onLoad();
    }
}

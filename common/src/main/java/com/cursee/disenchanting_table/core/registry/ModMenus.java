package com.cursee.disenchanting_table.core.registry;

import com.cursee.disenchanting_table.Constants;
import com.cursee.disenchanting_table.DisenchantingTable;
import com.cursee.disenchanting_table.core.world.inventory.DisenchantingMenu;
import com.cursee.disenchanting_table.core.world.inventory.DisenchantingTableMenu;
import com.cursee.disenchanting_table.platform.Services;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

import java.util.function.BiConsumer;

public class ModMenus {

    public static final MenuType<DisenchantingTableMenu> DISENCHANTING_TABLE = Services.PLATFORM.registerMenu(DisenchantingTableMenu::new, FeatureFlags.VANILLA_SET);
    public static final MenuType<DisenchantingMenu> DISENCHANTING_MENU = Services.PLATFORM.registerMenu(DisenchantingMenu::new, FeatureFlags.VANILLA_SET);

    public static void register(BiConsumer<MenuType<?>, ResourceLocation> consumer) {
        consumer.accept(DISENCHANTING_TABLE, DisenchantingTable.identifier(Constants.MOD_ID));
        consumer.accept(DISENCHANTING_MENU, DisenchantingTable.identifier("disenchanting_menu"));
    }
}

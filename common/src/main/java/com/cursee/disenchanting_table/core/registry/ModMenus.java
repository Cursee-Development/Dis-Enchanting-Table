package com.cursee.disenchanting_table.core.registry;

import com.cursee.disenchanting_table.Constants;
import com.cursee.disenchanting_table.DisEnchantingTable;
import com.cursee.disenchanting_table.core.world.inventory.AutoDisEnchantingMenu;
import com.cursee.disenchanting_table.core.world.inventory.ManualDisenchantingMenu;
import com.cursee.disenchanting_table.platform.Services;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

import java.util.function.BiConsumer;

public class ModMenus {

    public static final MenuType<AutoDisEnchantingMenu> AUTO_DISENCHANTING_TABLE = Services.PLATFORM.registerMenu(AutoDisEnchantingMenu::new, FeatureFlags.VANILLA_SET);
    public static final MenuType<ManualDisenchantingMenu> MANUAL_DISENCHANTING_TABLE = Services.PLATFORM.registerMenu(ManualDisenchantingMenu::new, FeatureFlags.VANILLA_SET);

    public static void register(BiConsumer<MenuType<?>, ResourceLocation> consumer) {
        consumer.accept(AUTO_DISENCHANTING_TABLE, DisEnchantingTable.identifier(Constants.MOD_ID + "_auto"));
        consumer.accept(MANUAL_DISENCHANTING_TABLE, DisEnchantingTable.identifier(Constants.MOD_ID + "_manual"));
    }
}

package com.cursee.disenchanting_table.core.registry;

import com.cursee.disenchanting_table.DisEnchantingTable;
import com.cursee.disenchanting_table.core.world.inventory.DisEnchantingTableMenu;
import com.cursee.disenchanting_table.core.world.inventory.HopperDisEnchantingTableMenu;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

import java.util.function.BiConsumer;

public class ModMenus {

    public static final MenuType<DisEnchantingTableMenu> DISENCHANTING_MENU = new MenuType<>(DisEnchantingTableMenu::new, FeatureFlags.VANILLA_SET);
    public static final MenuType<HopperDisEnchantingTableMenu> HOPPER_DISENCHANTING_MENU = new MenuType<>(HopperDisEnchantingTableMenu::new, FeatureFlags.VANILLA_SET);

    public static void register(BiConsumer<MenuType<?>, ResourceLocation> consumer) {
        consumer.accept(DISENCHANTING_MENU, DisEnchantingTable.identifier("disenchanting_menu"));
    }
}

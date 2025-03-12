package com.cursee.disenchanting_table;

import net.minecraft.resources.ResourceLocation;

public class DisEnchantingTable {

    public static void init() {}

    public static ResourceLocation identifier(String value) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, value);
    }
}
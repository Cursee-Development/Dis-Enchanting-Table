package com.cursee.disenchanting_table;

import com.cursee.monolib.core.sailing.Sailing;
import net.minecraft.resources.ResourceLocation;

public class DisenchantingTable {

    public static void init() {
        Sailing.register(Constants.MOD_ID, Constants.MOD_NAME, Constants.MOD_VERSION, Constants.MOD_PUBLISHER, Constants.MOD_URL);
    }

    public static ResourceLocation identifier(String path) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, path);
    }
}
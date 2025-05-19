package com.cursee.disenchanting_table;

import com.cursee.disenchanting_table.core.registry.ModRegistryFabric;
import net.fabricmc.api.ModInitializer;

public class DisenchantingTableFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        DisenchantingTable.init();
        ModRegistryFabric.register();
    }
}

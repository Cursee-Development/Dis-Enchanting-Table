package com.cursee.disenchanting_table;

import net.fabricmc.api.ClientModInitializer;

public class DisEnchantingTableClientFabric implements ClientModInitializer {
    
    @Override
    public void onInitializeClient() {
        DisEnchantingTableClient.init();
    }
}

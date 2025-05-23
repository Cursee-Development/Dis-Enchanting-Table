package com.cursee.disenchanting_table;

import com.cursee.disenchanting_table.core.network.FabricNetwork;
import com.cursee.disenchanting_table.core.network.packet.FabricConfigSyncS2CPacket;
import com.cursee.disenchanting_table.core.registry.ModRegistryFabric;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class DisenchantingTableFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        DisenchantingTable.init();
        ModRegistryFabric.register();
        ServerLifecycleEvents.SERVER_STARTING.register(DisenchantingTableServerFabric::new);

        FabricNetwork.init();
        ServerEntityEvents.ENTITY_LOAD.register(FabricConfigSyncS2CPacket::createAndSend);
    }
}

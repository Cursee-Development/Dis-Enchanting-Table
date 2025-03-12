package com.cursee.disenchanting_table;

import com.cursee.disenchanting_table.core.FabricCommonConfigHandler;
import com.cursee.disenchanting_table.core.network.packet.FabricConfigSyncS2CPacket;
import com.cursee.disenchanting_table.core.registry.FabricNetwork;
import com.cursee.disenchanting_table.core.registry.RegistryFabric;
import com.cursee.monolib.core.sailing.Sailing;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;

public class DisEnchantingTableFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        DisEnchantingTable.init();
        Sailing.register(Constants.MOD_ID, Constants.MOD_NAME, Constants.MOD_VERSION, Constants.MOD_PUBLISHER, Constants.MOD_URL);
        RegistryFabric.register();

        FabricCommonConfigHandler.onLoad();

        FabricNetwork.Packets.registerPacketIDs();
        ServerEntityEvents.ENTITY_LOAD.register(FabricConfigSyncS2CPacket::registerS2CPacketSender);
    }
}

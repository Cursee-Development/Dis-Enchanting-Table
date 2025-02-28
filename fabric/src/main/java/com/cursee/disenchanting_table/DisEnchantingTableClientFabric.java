package com.cursee.disenchanting_table;

import com.cursee.disenchanting_table.client.FabricClientConfigHandler;
import com.cursee.disenchanting_table.client.network.packet.FabricConfigSyncClientHandler;
import com.cursee.disenchanting_table.client.network.packet.FabricItemSyncClientHandler;
import com.cursee.disenchanting_table.core.registry.FabricNetwork;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class DisEnchantingTableClientFabric implements ClientModInitializer {
    
    @Override
    public void onInitializeClient() {
        DisEnchantingTableClient.init();
        FabricClientConfigHandler.onLoad();
        ClientPlayNetworking.registerGlobalReceiver(FabricNetwork.Packets.CONFIG_SYNC_S2C, FabricConfigSyncClientHandler::registerS2CPacketHandler);
        ClientPlayNetworking.registerGlobalReceiver(FabricNetwork.Packets.ITEM_SYNC_S2C, FabricItemSyncClientHandler::registerS2CPacketHandler);
    }
}

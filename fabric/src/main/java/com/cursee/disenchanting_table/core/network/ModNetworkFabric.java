package com.cursee.disenchanting_table.core.network;

import com.cursee.disenchanting_table.client.network.packet.FabricConfigSyncS2CPacketHandler;
import com.cursee.disenchanting_table.client.network.packet.FabricItemSyncS2CPacketHandler;
import com.cursee.disenchanting_table.core.registry.ModMessages;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ModNetworkFabric {

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(ModMessages.ITEM_SYNC_S2C, FabricItemSyncS2CPacketHandler::receive);
        ClientPlayNetworking.registerGlobalReceiver(ModMessages.CONFIG_SYNC_S2C, FabricConfigSyncS2CPacketHandler::receive);
    }
}

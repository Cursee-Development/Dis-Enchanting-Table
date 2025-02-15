package com.cursee.disenchanting_table.core.network;

import com.cursee.disenchanting_table.core.network.packet.FabricItemSyncS2CPacket;
import com.cursee.disenchanting_table.core.registry.ModMessages;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ModNetworkFabric {

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(ModMessages.ITEM_SYNC_S2C, FabricItemSyncS2CPacket::receive);
    }
}

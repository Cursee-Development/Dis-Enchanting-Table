package com.cursee.disenchanting_table;

import com.cursee.disenchanting_table.client.FabricClientConfigHandler;
import com.cursee.disenchanting_table.client.block.entity.renderer.FabricDisEnchantingBER;
import com.cursee.disenchanting_table.client.network.packet.FabricConfigSyncClientHandler;
import com.cursee.disenchanting_table.client.network.packet.FabricItemSyncClientHandler;
import com.cursee.disenchanting_table.core.network.packet.FabricConfigSyncS2CPacket;
import com.cursee.disenchanting_table.core.network.packet.FabricItemSyncS2CPacket;
import com.cursee.disenchanting_table.core.registry.FabricBlockEntities;
import com.cursee.disenchanting_table.core.registry.FabricNetwork;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class DisEnchantingTableClientFabric implements ClientModInitializer {
    
    @Override
    public void onInitializeClient() {
        DisEnchantingTableClient.init();
        FabricClientConfigHandler.onLoad();
        BlockEntityRenderers.register(FabricBlockEntities.DISENCHANTING_TABLE, FabricDisEnchantingBER::new);

//        ClientPlayNetworking.registerGlobalReceiver(FabricNetwork.Packets.CONFIG_SYNC_S2C, FabricConfigSyncClientHandler::registerS2CPacketHandler);
//        ClientPlayNetworking.registerGlobalReceiver(FabricNetwork.Packets.ITEM_SYNC_S2C, FabricItemSyncClientHandler::registerS2CPacketHandler);
        ClientPlayNetworking.registerGlobalReceiver(FabricNetwork.CONFIG_SYNC_ID, FabricConfigSyncS2CPacket::handle);
        ClientPlayNetworking.registerGlobalReceiver(FabricNetwork.ITEM_SYNC_ID, FabricItemSyncS2CPacket::handle);
    }
}

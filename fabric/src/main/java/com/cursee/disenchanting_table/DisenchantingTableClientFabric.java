package com.cursee.disenchanting_table;

import com.cursee.disenchanting_table.client.network.packet.FabricConfigSyncClientHandler;
import com.cursee.disenchanting_table.client.network.packet.FabricItemSyncClientHandler;
import com.cursee.disenchanting_table.client.renderer.blockentity.DisenchantingTableRenderer;
import com.cursee.disenchanting_table.core.network.FabricNetwork;
import com.cursee.disenchanting_table.core.registry.ModBlockEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class DisenchantingTableClientFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        DisenchantingTableClient.init();

        BlockEntityRenderers.register(ModBlockEntities.DISENCHANTING_TABLE, DisenchantingTableRenderer::new);

        ClientPlayNetworking.registerGlobalReceiver(FabricNetwork.Packets.CONFIG_SYNC_S2C, FabricConfigSyncClientHandler::receiveOnClient);
        ClientPlayNetworking.registerGlobalReceiver(FabricNetwork.Packets.ITEM_SYNC_S2C, FabricItemSyncClientHandler::receiveOnClient);
    }
}

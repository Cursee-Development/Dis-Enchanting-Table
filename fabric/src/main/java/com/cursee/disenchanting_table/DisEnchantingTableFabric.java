package com.cursee.disenchanting_table;

import com.cursee.disenchanting_table.core.CommonConfigValues;
import com.cursee.disenchanting_table.core.FabricCommonConfigHandler;
import com.cursee.disenchanting_table.core.network.packet.FabricConfigSyncS2CPacket;
import com.cursee.disenchanting_table.core.registry.FabricNetwork;
import com.cursee.disenchanting_table.core.registry.RegistryFabric;
import com.cursee.monolib.core.sailing.Sailing;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;

public class DisEnchantingTableFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        DisEnchantingTable.init();
        Sailing.register(Constants.MOD_ID, Constants.MOD_NAME, Constants.MOD_VERSION, Constants.MOD_PUBLISHER, Constants.MOD_URL);
        RegistryFabric.register();

        FabricCommonConfigHandler.onLoad();

        // FabricNetwork.Packets.registerPacketIDs();
        // ServerEntityEvents.ENTITY_LOAD.register(FabricConfigSyncS2CPacket::registerS2CPacketSender);

        PayloadTypeRegistry.playS2C().register(FabricNetwork.CONFIG_SYNC_ID, FabricNetwork.CONFIG_SYNC_CODEC);
        PayloadTypeRegistry.playS2C().register(FabricNetwork.ITEM_SYNC_ID, FabricNetwork.ITEM_SYNC_CODEC);
        ServerEntityEvents.ENTITY_LOAD.register((entity, level) -> {
            if (entity instanceof ServerPlayer serverPlayer) ServerPlayNetworking.send(serverPlayer, new FabricConfigSyncS2CPacket(CommonConfigValues.automatic_disenchanting, CommonConfigValues.resets_repair_cost, CommonConfigValues.requires_experience, CommonConfigValues.uses_points, CommonConfigValues.experience_cost));
        });
    }
}

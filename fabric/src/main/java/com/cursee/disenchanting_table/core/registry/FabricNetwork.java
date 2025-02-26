package com.cursee.disenchanting_table.core.registry;

import com.cursee.disenchanting_table.DisEnchantingTable;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class FabricNetwork {

    public static class Packets {
        public static final ResourceLocation CONFIG_SYNC_S2C = DisEnchantingTable.identifier("config_sync");

        public static void registerPacketIDs() {}
    }

    public static void sendToPlayer(FriendlyByteBuf data, ServerPlayer player, ResourceLocation packetID) {
        ServerPlayNetworking.send(player, packetID, data);
    }
}

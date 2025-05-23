package com.cursee.disenchanting_table.core.network;

import com.cursee.disenchanting_table.DisenchantingTable;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class FabricNetwork {

    public static class Packets {
        public static final ResourceLocation CONFIG_SYNC_S2C = DisenchantingTable.identifier("config_sync");
        public static final ResourceLocation ITEM_SYNC_S2C = DisenchantingTable.identifier("item_sync");
    }

    public static void init() {}

    public static void sendToPlayer(FriendlyByteBuf data, ServerPlayer player, ResourceLocation packetID) {
        ServerPlayNetworking.send(player, packetID, data);
    }

    public static void sendToServer(FriendlyByteBuf data, ResourceLocation packetID) {
        ClientPlayNetworking.send(packetID, data);
    }
}

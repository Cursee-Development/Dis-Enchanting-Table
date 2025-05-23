package com.cursee.disenchanting_table.client.network.packet;

import com.cursee.disenchanting_table.core.ServerConfig;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;

public class FabricConfigSyncClientHandler {

    public static void receiveOnClient(Minecraft client, ClientPacketListener handler, FriendlyByteBuf data, PacketSender responseSender) {
        ServerConfig.automatic_disenchanting = data.readBoolean();
        ServerConfig.resets_repair_cost = data.readBoolean();
        ServerConfig.requires_experience = data.readBoolean();
        ServerConfig.uses_points = data.readBoolean();
        ServerConfig.experience_cost = data.readInt();
    }
}

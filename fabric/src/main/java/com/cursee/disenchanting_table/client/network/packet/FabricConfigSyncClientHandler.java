package com.cursee.disenchanting_table.client.network.packet;

import com.cursee.disenchanting_table.core.CommonConfigValues;
import com.cursee.disenchanting_table.core.network.packet.FabricConfigSyncS2CPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;

public class FabricConfigSyncClientHandler {

//    public static void registerS2CPacketHandler(Minecraft client, ClientPacketListener handler, FriendlyByteBuf data, PacketSender responseSender) {
//        CommonConfigValues.automatic_disenchanting = data.readBoolean();
//        CommonConfigValues.resets_repair_cost = data.readBoolean();
//        CommonConfigValues.requires_experience = data.readBoolean();
//        CommonConfigValues.uses_points = data.readBoolean();
//        CommonConfigValues.experience_cost = data.readInt();
//    }

    public static void handle(FabricConfigSyncS2CPacket packet, ClientPlayNetworking.Context context) {
        CommonConfigValues.automatic_disenchanting = packet.automatic_disenchanting;
        CommonConfigValues.resets_repair_cost = packet.resets_repair_cost;
        CommonConfigValues.requires_experience = packet.requires_experience;
        CommonConfigValues.uses_points = packet.uses_points;
        CommonConfigValues.experience_cost = packet.experience_cost;
    }
}
